/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.io.*;
import java.net.*;
import Game.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import tcpserver.*;

/**
 *
 * @author Wilhelm
 */
public class ConnectionHandler implements Runnable{

    private int id;
    private int state;
    private int num_players;
    private Socket server;
    private String line,input;
    
    public ConnectionHandler(int _id,Socket _server,int _num_players){
        this.server = _server;
        this.id = _id;
        this.num_players= _num_players;
    }
    
    private boolean isMyTurn(){
        if(Main.Brd.getTurn()==this.id){
            return true;
        }
        else return false;
    }
  
    private boolean isStateChanged(){
        if (state==Main.Brd.getState()){
            return false;
        }
        
        else{
            this.state = Main.Brd.getState();
            return true;
        }
    }
    
    @Override
    public void run(){
        
        try{
            
        //INITIALIZATION
        DataOutputStream out = new DataOutputStream(server.getOutputStream());
        //sending the number of players for client's board initialization
        out.writeUTF(Integer.toString(this.num_players));
        //asking the username
        out.writeUTF("Please enter your name to play the game:");
        DataInputStream in = new DataInputStream(server.getInputStream());
        String user = in.readUTF();
        System.out.println(user+" has joined the game");
        Player player = new Player(this.id,user);
        Main.Brd.addPlayer(player);
        out.writeUTF(Integer.toString(this.id));
        
        //WAITING UNTIL EVERY PLAYERS ARE READY
        while(!Main.Brd.isEnoughPlayer()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        out.writeUTF("And The Game is On!");
        
        while(!Main.Brd.isWin()){
            if(isStateChanged()){
                out.writeUTF(Main.Brd.getRecentMoves());
            }
            
            if(!isMyTurn()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            else{
                //signal the client that it is their move
                out.writeUTF("Move");
                out.writeUTF("Your Move :  ");
                String move = in.readUTF();
                move = move.split("[\\(\\)]")[1];
                String[] coordinate = move.split(",");
                int x = Integer.parseInt(coordinate[0]);
                int y = Integer.parseInt(coordinate[1]);
                //memasukkan move ke dalam tabel
                Main.Brd.setBoardElement(x, y, this.id);
                Main.Brd.nextMove();
            }
        }
        
        server.close();
        
        } catch (IOException ioe) {
            System.out.println("IOException on socket listen: " + ioe);
            ioe.printStackTrace();
        }
    }
}

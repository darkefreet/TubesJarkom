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
    private Socket server;
    private String line,input;
    
    public ConnectionHandler(int _id,Socket _server){
        this.server = _server;
        this.id = _id;
    }
    
    private boolean isMyTurn(){
        if(Main.Brd.getTurn()==this.id){
            return true;
        }
        else return false;
    }
    
    @Override
    public void run(){
        
        try{
        DataOutputStream out = new DataOutputStream(server.getOutputStream());
        out.writeUTF("Please enter your name to play the game:");
            
        DataInputStream in = new DataInputStream(server.getInputStream());
        String user = in.readUTF();
        System.out.println(user+" has joined the game");
        
        Player player = new Player(this.id,user);
        Main.Brd.addPlayer(player);
        
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
            while(!isMyTurn() && !Main.Brd.isWin()){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            if(!Main.Brd.isWin()){
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
        
        out.writeUTF("End");
        server.close();
        
        } catch (IOException ioe) {
            System.out.println("IOException on socket listen: " + ioe);
            ioe.printStackTrace();
        }
    }
}

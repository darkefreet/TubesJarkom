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

    private Socket server;
    private String line,input;
    private Player player;
    private String state;
    private int game_state;
    private int room_number = 0;
    
    //nama-nama status
    private final String init = "INIT";
    private final String inHall = "HALL";
    private final String roomUpdate = "UPDATE ROOM";
    private final String inGame = "GAME";
    private final String startGame = "START";
    private final String yourmove = "MOVE";
    
    public ConnectionHandler(int _id,Socket _server){
        this.server = _server;
        this.player = new Player();
        player.setID(_id);
    }
    
    public boolean isMyTurn(){
        if(Server.room.getRoom(room_number).getTurn()==player.getID()){
            return true;
        }
        else return false;
    }
    
    public boolean isStateChanged(){
        if(Server.room.getRoom(room_number).getState()==this.game_state){
            return false;
        }
        else{
            game_state = Server.room.getRoom(room_number).getState();
            return true;
        }
    }
    
    @Override
    public void run(){
        
        try{    
        //INITIALIZATION
        DataOutputStream out = new DataOutputStream(server.getOutputStream());
        DataInputStream in = new DataInputStream(server.getInputStream());
        
        //ASKING USERNAME
        //mengirimkan status pada user
        out.writeUTF(init);
        String input = in.readUTF();
        System.out.println(input+" is online");
        player.setName(input);
        state = inHall;
        
        while(true){
            if(state.equals(inHall)){
                if(Server.needRoomUpdate){
                    out.writeUTF(roomUpdate);
                    out.writeUTF(Server.room.roomState());
                }
            
                //ALREADY IN HALL, WAIT FOR ACTION<CREATE OR JOIN> A ROOM
                out.writeUTF(inHall);
                input =in.readUTF();
                
                String parse[] = input.split(" ");
                if(parse[0].equals("CREATE")){
                    //System.out.println(input);
                    Server.CreateRoom(Integer.parseInt(parse[1]));
                    //System.out.println(Server.room.roomState());
                }
                else if(parse[0].equals("JOIN")){//JOIN A ROOM
                    //System.out.println(input);
                    
                    //Put the user in a room
                    room_number = Integer.parseInt(parse[1]);
                    Server.room.getRoom(room_number).addPlayer(this.player);
                    state = inGame;
                }
                else{
                    //do nothing.just refresh
                }
            }
            else if(state.equals(inGame)){
                out.writeUTF(inGame);
                out.writeUTF(Integer.toString(room_number));
                out.writeUTF(Integer.toString(Server.room.getRoom(room_number).getNum_players()));
                //System.out.println("Jumlah pemain_maksimal : " +Server.room.getRoom(room_number).getNum_players());
                //WAIT UNTIL THE SERVER HAS ENOUGH PLAYERS
                while(!Server.room.getRoom(room_number).isEnoughPlayer()){
                    try {
                        Thread.sleep(5000);
                        System.out.println(Server.room.getRoom(room_number).getListPlayers().size());
                    } catch (InterruptedException e) {
                         Thread.currentThread().interrupt();
                    return;
                }
                }
                
                //System.out.println("Game Start");
                //SEND THE BOARD DATA AFTER ENOUGH PLAYERS HAVE JOINED THE GAME
                out.writeUTF(startGame);
                out.writeUTF(Server.room.getRoom(room_number).getPlayers());
                
                //PLAYERS CANNOT GET OUT BEFORE THE BOARD IS WON
                while(!Server.room.getRoom(room_number).getStatusWin()){
                    if(isStateChanged()){
                        out.writeUTF(Server.room.getRoom(room_number).LastMove());
                    }
                    if(isMyTurn()){
                        //signal the client that it is their move
                        System.out.println("masuk sini");
                        out.writeUTF(yourmove);
                        String move = in.readUTF();
                        move = move.split("[\\(\\)]")[1];
                        String[] coordinate = move.split(",");
                        int x = Integer.parseInt(coordinate[0]);
                        int y = Integer.parseInt(coordinate[1]);
                        //memasukkan move ke dalam tabel
                        Server.room.getRoom(room_number).setBoardElement(x, y, this.player.getID());
                        Server.room.getRoom(room_number).nextMove();
                    }
                }
                out.writeUTF(inHall);
            }
        }
        
        /*
        if(!reconnect){
            
            
            Server.Brd.addPlayer(player);
            out.writeUTF(Integer.toString(this.id));

            //WAITING UNTIL EVERY PLAYERS ARE READY
            while(!Server.Brd.isEnoughPlayer()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            out.writeUTF("And The Game is On!");
        }
        else{//reconnect
            out.writeUTF("Reconnecting");
            out.writeUTF(Integer.toString(Server.Brd.getRecentMoves().size()));
            for(int i =1;i<=Server.Brd.getState();i++){
                out.writeUTF(Server.Brd.getRecentMoves().get(i));
            }
            out.writeUTF("Reconnecting is done");
        }
          
        while(!Server.Brd.isWin()){
            if(isStateChanged()){
                out.writeUTF(Server.Brd.LastMove());
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
                Server.Brd.setBoardElement(x, y, this.id);
                Server.Brd.nextMove();
            }
        }*/
        
        //server.close();
        
        } catch (IOException ioe) {
            System.out.println("Client with id " + this.player.getID()+" is disconnected.");
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpclient;

import Game.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Wilhelm
 */
public class Main {

    private static int user_id;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
      String serverName = "localhost";
      int port = 8081;
      try
      {
         Socket client = new Socket(serverName, port);
         InputStream inFromServer = client.getInputStream();
         DataInputStream in = new DataInputStream(inFromServer);
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out = new DataOutputStream(outToServer); 
         
         String move;
         Scanner input = new Scanner(System.in);
         String s;
         //first sent message is the number of players
         int num_users = Integer.parseInt(in.readUTF());
         Board Brd = new Board(20,num_users);
            
         //RECONNECTING BY RECEIVING GAME STATES UNTIL THE POINT WHERE CONNECTION IS LOST
         if(in.readUTF().equals("Reconnecting")){
             System.out.println("Loading your data");
             int state = Integer.parseInt(in.readUTF());
             for(int i =1;i<state;i++){
                move = in.readUTF();
                move = move.split("[\\(\\)]")[1];
                String[] coordinate = move.split(",");
                int x = Integer.parseInt(coordinate[0]);
                int y = Integer.parseInt(coordinate[1]);
                int el = Integer.parseInt(coordinate[2]);
                
                Brd.setBoardElement(x, y, el);
                //not allowed for last element
                if(i!=state-1){
                    Brd.nextMove();
                }
             }
         
         }else{

            //print the order to enter username
            System.out.println(in.readUTF());

            //get username input
            s = input.nextLine();
            out.writeUTF(s);

            //get user ID and insert players to the board
            user_id = Integer.parseInt(in.readUTF());
            for(int i = 1; i<=Brd.getNum_players();i++){
                if(i==user_id){
                    Player player = new Player(user_id,s);
                    Brd.addPlayer(player);
                }
                else{
                    Player player = new Player(i,"Player "+i);
                    Brd.addPlayer(player);
                }
            }
         }
        
         System.out.println(Brd.getRecentMoves());
         //Game Start message
         System.out.println(in.readUTF());
         
         while(!Brd.isWin()){
             move = in.readUTF();
             if(!move.equals("Move")){
                
                move = move.split("[\\(\\)]")[1];
                String[] coordinate = move.split(",");
                int x = Integer.parseInt(coordinate[0]);
                int y = Integer.parseInt(coordinate[1]);
                int el = Integer.parseInt(coordinate[2]);
                
                System.out.println("Player with id = "+el+" choose "+"("+x+","+y+")");
                Brd.setBoardElement(x, y, el);
                Brd.nextMove();
             }
             else{
                 String msg = in.readUTF();
                 System.out.println(msg);
                
                 s = input.nextLine();
                 out.writeUTF(s);
             }
         }
         
         client.close();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
        
    }
    
}

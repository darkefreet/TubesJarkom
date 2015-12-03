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
public class Client {
    private static int user_id;
    private static String state = "0"; //merupakan status-status dari board yang ada pada room
    private static String message = "START"; //berisikan message dari server
    private static DataInputStream in;
    private static DataOutputStream out;
    
    
    private static void updateRoom(String r_state){
        if(r_state.equals(state)){
            //do nothing tidak ada room yang bertambah atau selesai
        }
        else{
            state = r_state;
            String r[] = r_state.split(" ");
            int num_rooms = Integer.parseInt(r[0]);
            System.out.println("\nHere is the list of Rooms Available");
            for(int i=1;i<=num_rooms;i++){
                if (r[i].equals("1")){
                    System.out.println("Room "+(i-1)+ "  - available\n");
                }
                else{
                    System.out.println("Room "+(i-1)+ "  - has been closed\n");
                }
            }
        }
    }
    
    private static void React() throws IOException{
        Scanner input = new Scanner(System.in);
        String s;
        String room_state;
        switch (message) {
            case "INIT":
                System.out.println("Please input your username: ");
                s=input.nextLine();
                out.writeUTF(s);
                break;
            case "HALL":
                System.out.println("You are now in the Main Room");
                System.out.println("Choose something from the option by write the Option in parentheses!");
                System.out.println("1.Create Room (CREATE) <num_of_players>");
                System.out.println("2.Join Room (JOIN <room_number>)");
                System.out.println("3.Refresh Room List (REFRESH)");
                s=input.nextLine();
                out.writeUTF(s);
                break;
            case "UPDATE ROOM":
                room_state = in.readUTF();
                updateRoom(room_state);
                break;
            case "GAME":
                break;
            default:
                //do nothing
        }
    }
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
      String serverName = "localhost";
      int port = 8081;
      try
      {
          //INISIALISASI
            Socket client = new Socket(serverName, port);
            InputStream inFromServer = client.getInputStream();
            OutputStream outToServer = client.getOutputStream();  
            in = new DataInputStream(inFromServer);
            out = new DataOutputStream(outToServer);

            while(!message.equals("END")){
                message = in.readUTF();
                React();
            }  
          /*
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
         */
         client.close();
      }catch(IOException e)
      {
         //e.printStackTrace();
      }
        
    }
    
}

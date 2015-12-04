/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpclient;

import Game.*;
import UI.Lobby;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Wilhelm
 */
public class Client {
    public static boolean button_pressed = false;
    private static int user_id;
    public static String input;
    private static String state = "0"; //merupakan status-status dari board yang ada pada room
    private static String message = "ALIVE"; //berisikan message dari server
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Board board;
    
    private static WelcomePage welco;
    private static Lobby lobby;
    public static int num_rooms;
    
    
    private static void updateRoom(String r_state){
        //MENGUPDATE ROOM DAN MENAMPILKAN LIST ROOM
        if(r_state.equals(state)){
            //do nothing tidak ada room yang bertambah atau selesai
        }
        else{
            state = r_state;
            String r[] = r_state.split(" ");
            num_rooms = Integer.parseInt(r[0]);
            System.out.println("\nHere is the list of Rooms Available");
            for(int i=1;i<=num_rooms;i++){
                String room_state[] = r[i].split(",");
                System.out.println("Room "+(i-1)+" - number of players in game : "+room_state[1]);
                System.out.println("Maximum number of players : "+room_state[0]+"\n");
            }
        }
    }
    
    private static void React() throws IOException{
        String room_state;
        switch (message) {
            case "INIT":
                System.out.println("Please input your username: ");
                while(!button_pressed){
                    try {
                    Thread.sleep(1000);
                        //System.out.println(Server.room.getRoom(room_number).getListPlayers().size());
                    } catch (InterruptedException e) {
                         Thread.currentThread().interrupt();
                    return;
                    }
                }
                button_pressed = false;
                out.writeUTF(input);
                welco.setVisible(false);
                lobby = new Lobby();
                lobby.setVisible(true);
                break;
            case "HALL":
                System.out.println("You are now in the Main Room");
                System.out.println("Choose something from the option by write the Option in parentheses!");
                System.out.println("1.Create Room (CREATE) <num_of_players>");
                System.out.println("2.Join Room (JOIN <room_number>)");
                System.out.println("3.Refresh Room List (REFRESH)");
                while(!button_pressed){
                    try {
                        Thread.sleep(1000);
                            //System.out.println(Server.room.getRoom(room_number).getListPlayers().size());
                        } catch (InterruptedException e) {
                             Thread.currentThread().interrupt();
                        return;
                    }
                }
                button_pressed = false;
                out.writeUTF(input);
                break;
            case "UPDATE ROOM":
                room_state = in.readUTF();
                updateRoom(room_state);
                break;
            case "GAME":
                int id_board = Integer.parseInt(in.readUTF());
                int num_players = Integer.parseInt(in.readUTF());
                board = new Board(id_board, num_players);
                break;
            case "START":
                //GET ALL BOARD STATUS
                System.out.println("Game has been started");
                String BoardStatus []= in.readUTF().split(" ");
                for (int i = 0; i<board.getNum_players();i++){
                    String Player[] = BoardStatus[i].split(",");
                    Player newPlayer = new Player(Integer.parseInt(Player[0]),Player[1]);
                    board.addPlayer(newPlayer);
                }
                System.out.println("Users data has been loaded");
                while(!board.getStatusWin()){
                    String move = in.readUTF();
                    if(!move.equals("MOVE")){
                        move = move.split("[\\(\\)]")[1];
                        String[] coordinate = move.split(",");
                        int x = Integer.parseInt(coordinate[0]);
                        int y = Integer.parseInt(coordinate[1]);
                        int el = Integer.parseInt(coordinate[2]);

                        System.out.println("Player with id = "+el+" choose "+"("+x+","+y+")");
                        board.setBoardElement(x, y, el);
                        board.nextMove();
                     }
                     else{
                         System.out.println("Now is Your Move. Pick a move: ");
                         while(!button_pressed){
                            try {
                                Thread.sleep(1000);
                                    //System.out.println(Server.room.getRoom(room_number).getListPlayers().size());
                                } catch (InterruptedException e) {
                                     Thread.currentThread().interrupt();
                                return;
                            }
                        }
                        button_pressed = false;
                        out.writeUTF(input);
                     }
                }
                break;
            default:
                //do nothing
        }
    }
   
    /**
     * @param args the command line arguments
     */
    private static WelcomePage welco;
    public static void main(String[] args) {
        // TODO code application logic here
        
      String serverName = "localhost";
      int port = 8081;
      try
      {
          //INISIALISASI

            welco.setVisible(true);
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

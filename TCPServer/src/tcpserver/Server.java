/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;
import Game.*;
import Connection.*;
import java.io.*;
import static java.lang.Thread.sleep;
import java.net.*;
/**
 *
 * @author Wilhelm
 */
public class Server {
    private static int id_room = 0;
    public static boolean needRoomUpdate = true;
    private static int port=8081;
    public static Hall room = new Hall();
    
    public static void CreateRoom(int num_players){
        Board newBoard;
        newBoard = new Board(id_room, num_players);
        room.addRoom(newBoard);
        id_room++;
        //System.out.println(id_room);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)throws IOException {
        // TODO code application logic here
        
        try{
            ServerSocket listener = new ServerSocket(port);
            Socket server;
            int i = 1;
            System.out.println("Server has been set up");
            //Open 50 Sockets for players and watchers
            while(i<=50){    
                server = listener.accept();
                ConnectionHandler connection= new ConnectionHandler(i,server);
                Thread t = new Thread(connection);
                t.start();
                i++;
            }
            while(true){
                //do nothing
            } 
        } catch (IOException ioe) {
          System.out.println("IOException on socket listen: " + ioe);
          ioe.printStackTrace();
        }
    }
}

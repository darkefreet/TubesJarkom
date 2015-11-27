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
public class Main {
    private static int port=8081;
    public static Board Brd = new Board();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try{
            ServerSocket listener = new ServerSocket(port);
            Socket server;
            System.out.println("Starting the Game");
            
            int i = 1;
            
            //Waiting until there are enough number of players to start the game
            while(i<=Brd.getNum_players()){
                
                server = listener.accept();
                
                ConnectionHandler connection= new ConnectionHandler(i,server);
                Thread t = new Thread(connection);
                t.start();
                i++;
                //System.out.println("Halo1");
            }
            while(!Brd.isEnoughPlayer()){
                System.out.println("waiting for players...");
                try {
                    // to sleep 5 seconds
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            System.out.println("Number of players now :" + Brd.getPlayers().size());
            for(Player e:Brd.getPlayers()){
                System.out.println("Players name: "+ e.getName());
            }
            listener.close();
        } catch (IOException ioe) {
          System.out.println("IOException on socket listen: " + ioe);
          ioe.printStackTrace();
        }
        
    }
}

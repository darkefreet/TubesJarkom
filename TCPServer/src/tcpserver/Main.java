/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;
import Game.*;
import Connection.*;
import java.io.*;
import java.net.*;
/**
 *
 * @author Wilhelm
 */
public class Main {
    private static int port=8081, maxConnections=0;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Board A = new Board();
        
        try{
            ServerSocket listener = new ServerSocket(port);
            Socket server;
            int i = 0;
            
            while((i++ < maxConnections) || (maxConnections == 0)){
                System.out.println("Halo");
                server = listener.accept();
                ConnectionHandler connection= new ConnectionHandler(server);
                Thread t = new Thread(connection);
                t.start();
                System.out.println("Halo1");
            }
            
            listener.close();
        } catch (IOException ioe) {
          System.out.println("IOException on socket listen: " + ioe);
          ioe.printStackTrace();
        }
    }
}

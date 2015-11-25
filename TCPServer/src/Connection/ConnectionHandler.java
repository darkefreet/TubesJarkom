/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.io.*;
import java.net.*;
import Game.*;
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
        server.close();
        
        } catch (IOException ioe) {
            System.out.println("IOException on socket listen: " + ioe);
            ioe.printStackTrace();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Wilhelm
 */
public class ConnectionHandler implements Runnable{

    private Socket server;
    private String line,input;
    
    public ConnectionHandler(Socket _server){
        this.server = _server;
    }
    
    @Override
    public void run(){
        
        try{
        DataInputStream in = new DataInputStream(server.getInputStream());
        System.out.println(in.readUTF());
        DataOutputStream out = new DataOutputStream(server.getOutputStream());
        out.writeUTF("Thank you for connecting to "+ server.getLocalSocketAddress() + "\nGoodbye!");
        server.close();
        
        } catch (IOException ioe) {
            System.out.println("IOException on socket listen: " + ioe);
            ioe.printStackTrace();
        }
    }
}

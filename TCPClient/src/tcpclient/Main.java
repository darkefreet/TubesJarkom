/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpclient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Wilhelm
 */
public class Main {

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
         System.out.println(in.readUTF());
         
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out = new DataOutputStream(outToServer);
         Scanner input = new Scanner(System.in);
         String s;
         s = input.nextLine();
         out.writeUTF(s);
         
         
         System.out.println(in.readUTF());
         boolean end = false;
         while(!end){
             String msg = in.readUTF();
             if(msg.equals("End")){
                end = true;
             }
             else{
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

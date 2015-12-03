/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.ArrayList;

/**
 *
 * @author Wilhelm
 */
public class Hall {

    private ArrayList<Board> Rooms;
    public Hall(){
        Rooms = new ArrayList<Board>();
    }
    
    public ArrayList<Board> getRooms(){
        return Rooms;
    }
    
    public Board getRoom(int n){
        return Rooms.get(n);
    }
    
    public void addRoom(Board b){
        Rooms.add(b);
    }
    
    public String roomState(){
        String s = Integer.toString(Rooms.size())+" ";
        for (Board e : Rooms){
            if(e.getStatusWin()){
                s = s+"0 ";
            }
            else{
                s = s+"1 ";
            }
        }
        return s;
    }
}

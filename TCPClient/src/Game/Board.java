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
public class Board {
    private int ID;
    private int state = 0;
    private final int size = 20;
    private int squares[][];
    private int num_players;
    private ArrayList<Player> Players;
    private boolean connected_players[];
    private int turn;
    private boolean won = false;
    private ArrayList<String> recent_moves;
    
    
    public Board(int id){
        squares = new int[size][size];
        for(int i = 0; i<size;i++){
            for(int j = 0; j<size; j++){
                squares[i][j]=0;
            }
        }
        state = 0;
        num_players = 3;
        connected_players = new boolean[num_players];
        for(int i = 0;i<num_players;i++){
            connected_players[i] = false; 
        }
        Players = new ArrayList<Player>();
        recent_moves = new ArrayList<String>();
        String s = "(0,0,0)";
        recent_moves.add(s);
        turn = 1;
    }
    
    public Board(int id, int num_players){
        squares = new int[size][size];
        for(int i = 0; i<size;i++){
            for(int j = 0; j<size; j++){
                squares[i][j]=0;
            }
        }
        num_players = num_players;
        connected_players = new boolean[num_players];
        for(int i = 0;i<num_players;i++){
            connected_players[i] = false; 
        }
        state = 0;
        Players = new ArrayList<Player>();
        recent_moves = new ArrayList<String>();
        String s = "(0,0,0)";
        recent_moves.add(s);
        turn = 1;
    }
    
    public ArrayList<String> getRecentMoves(){
        return recent_moves;
    }
    
    public String LastMove(){
        return recent_moves.get(recent_moves.size()-1);
    }
    
    public int getState(){
        return this.state;
    }
    
    public String getPlayers(){
        String s = null;
        for (Player e : Players){
            s = s+Integer.toString(e.getID())+","+e.getName()+" "; 
        }
        return s;
    }
    
    public int getBoardElement(int i, int j){
        return this.squares[i][j];
    }
    
    public void setBoardElement(int i, int j, int el){
        this.squares[i][j] = el;
        recent_moves.add("("+Integer.toString(i)+","+Integer.toString(j)+","+Integer.toString(el)+")");
    }
    public int getNum_players(){
        return num_players;
    }
    
    public void setNum_players(int n){
        this.num_players = n;
    }
    
    public boolean isEnoughPlayer(){
        if(Players.size()>=num_players){
            return true;}
        else
            return false;
    }
    
    public ArrayList<Player> getListPlayers(){
        return this.Players;
    }
    
    public void setPlayers(ArrayList<Player> p){
        this.Players = p;
    }
    
    public int getTurn(){
        //MENGEMBALIKAN ID PLAYER YANG MENJADI PEMAIN DI GAME TERSEBUT
        return Players.get(turn-1).getID();
    }
    
    public void setTurn(int i){
        this.turn = i;
    }
    
    public Player getPlayer(int i){
        return Players.get(i);
    }
    
    public void addPlayer(Player A){
        Players.add(A);
    }
    
    public void nextMove(){
        this.state++;
        this.turn++;
        if(turn>num_players){
            this.turn = 1;
        }
    }
    public void setWin(){
        won = true;
    }
    
    public boolean isMoveAllowed(int x, int y){
        if(squares[x][y]==0 &&(x<size) && (y<size)){
            return true;
        }
        else
            return false;
    }
    
    public boolean getConnectedStatus(int id){
        return connected_players[id-1];
    }
    
    public void setConnectedStatus(int id, boolean status){
        this.connected_players[id-1] = status;
    }
    public boolean getStatusWin(){
        return won;
    }
    
    public boolean isWin(){
        if(false){
            won = true;
        }
        return false;
    }
        
}

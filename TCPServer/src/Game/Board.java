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
        ID=id;
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
    
    public Board(int id, int _num_players){
        ID=id;
        squares = new int[size][size];
        for(int i = 0; i<size;i++){
            for(int j = 0; j<size; j++){
                squares[i][j]=0;
            }
        }
        num_players = _num_players;
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
        if(Players.size()>=num_players)
            return true;
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
        if(turn > num_players) turn = 1;
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
        won = false;
        if(checkHorizontal(this.squares)){
            won =  true;
        } else if(checkVertical(this.squares)){
            won = true;
        }else if(checkLD(this.squares)){
            won = true;
        }else if(checkRD(this.squares)){
            won = true;
        }
        return won;
    }
    
    public String getLastUser(){
        String move = LastMove();
        move = move.split("[\\(\\)]")[1];
        String[] coordinate = move.split(",");
        int el = Integer.parseInt(coordinate[2]);
        for(int i = 0;i<Players.size();i++){
            if(el==Players.get(i).getID()){
                return Players.get(i).getName();
            }
        }
        return null;
    }
    
    public  boolean checkHorizontal(int[][] a){
        
        for (int i =0; i<size;i++){
            for(int j=0;j<=size-5;j++){
                if ( a[i][j]!=0 && a[i][j]==a[i][j+1] && a[i][j]==a[i][j+2] && a[i][j]==a[i][j+3] && a[i][j]==a[i][j+4]){
                    return true;
                }
            }
            
        }
        return false;
    }
    
    public boolean checkVertical(int[][] a){
        
        for (int j =0; j<size;j++){
            for(int i=0;i<=size-5;i++){
                if ( a[i][j]!=0 && a[i][j]==a[i][j] && a[i][j]==a[i][j] && a[i][j]==a[i+3][j] && a[i][j]==a[i+4][j]){
                    return true;
                }
            }
            
        }
        return false;
    }
    
    public boolean checkLD(int[][] a){
        
        for (int i =0; i<=size-5;i++){
            for(int j=0;j<=size-5;j++){
                if ( a[i][j]!=0 && a[i][j]==a[i+1][j+1] && a[i][j]==a[i+2][j+2] && a[i][j]==a[i+3][j+3] && a[i][j]==a[i+4][j+4]){
                    return true;
                }
            }
            
        }
        return false;
    }
    
    public boolean checkRD(int[][] a){
        //System.out.println("Debug()");
        for (int i =4; i<size;i++){
            for(int j=0;j<=size-5;j++){
                //System.out.println(a[i][j] +" " + a[i-1][j+1] +" " + a[i-2][j+2] +" " + a[i-3][j+3] +" " + a[i-4][j+4]);
                if ( a[i][j]!=0 && a[i][j]==a[i-1][j+1] && a[i][j]==a[i-2][j+2] && a[i][j]==a[i-3][j+3] && a[i][j]==a[i-4][j+4]){
                    return true;
                }
            }
            
        }
        return false;
    }
        
}

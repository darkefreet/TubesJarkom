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
    
    private int squares[][];
    private int num_players;
    private ArrayList<Player> Players;
    public int turn;
    public String recent_moves;
    
    public Board(){
        squares = new int[20][20];
        for(int i = 0; i<20;i++){
            for(int j = 0; j<20; j++){
                squares[i][j]=0;
            }
        }
        num_players = 3;
        Players = new ArrayList<Player>();
        recent_moves = "(0,0,0)";
        turn = 1;
    }
    
    public Board(int size , int num_players){
        squares = new int[size][size];
        for(int i = 0; i<size;i++){
            for(int j = 0; j<size; j++){
                squares[i][j]=0;
            }
        }
        num_players = num_players;
        Players = new ArrayList<Player>();
        recent_moves = "(0,0,0)";
        turn = 1;
    }
    
    public String getRecentMoves(){
        return recent_moves;
    }
    
    public int getBoardElement(int i, int j){
        return this.squares[i][j];
    }
    
    public void setBoardElement(int i, int j, int el){
        this.squares[i][j] = el;
        recent_moves = "("+Integer.toString(i)+","+Integer.toString(j)+","+Integer.toString(el)+")";
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
    
    public ArrayList<Player> getPlayers(){
        return this.Players;
    }
    
    public void setPlayers(ArrayList<Player> p){
        this.Players = p;
    }
    
    public int getTurn(){
        return turn;
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
        this.turn++;
        if(turn>Players.size()){
            this.turn = 1;
        }
    }
    
    public boolean isWin(){
        return false;
    }
        
}

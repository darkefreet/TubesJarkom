/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

/**
 *
 * @author Wilhelm
 */
public class Player {
    private int id;
    private String name;
    private String icon;
    
    public Player(){
        id = 0;
        name = "default";
        icon = "icon"+id;
    }
    
    public Player(int id,String username){
        id = id;
        name = username;
        icon = "icon"+id;
    }
    
    public int getID(){
        return id;
    };
    
    public void setID(int id){
        this.id = id;
    };
    
    public String getName(){
        return name;
    };
    
    public void setName(String _name){
        this.name = _name;
    };
    
    public String getIcon(){
        return icon;
    };
    
    public void setIcon(String _icon){
        this.icon = _icon;
    };
}

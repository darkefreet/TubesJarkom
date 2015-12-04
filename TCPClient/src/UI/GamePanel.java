/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import tcpclient.Client;

/**
 *
 * @author LUCKY
 */
public class GamePanel extends JPanel implements MouseListener {
    static int DIMENSION = 20;
    static int BOARDDIMENSION = 500;
    static int LABELSIZE = 50;

    ArrayList<ArrayList<JLabel>> al= new ArrayList<>();
    public GamePanel() {
        setLayout(new GridLayout(20,20));
        for(int i = 0;i<DIMENSION;i++){
            ArrayList<JLabel> temp = new ArrayList<>();
            for(int j = 0;j < DIMENSION; j++){
                JLabel label = new JLabel("",SwingConstants.CENTER);
                temp.add(label);
            }
            al.add(temp);
        }
        for(int i = 0;i < DIMENSION;i++){
            for(int j = 0;j < DIMENSION; j++){
                al.get(i).get(j).setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                al.get(i).get(j).setSize(new Dimension(LABELSIZE,LABELSIZE));
                al.get(i).get(j).addMouseListener(this);
                al.get(i).get(j).putClientProperty("Location", new Point(i,j));
                add(al.get(i).get(j));
            }
        }
        setVisible(true);
        setSize(BOARDDIMENSION,BOARDDIMENSION);
    }
    
    public void refresh(){
        for( int i = 0; i < DIMENSION; i++){
            for (int j = 0 ; j < DIMENSION ; j++){
                if (Client.board.getBoardElement(i, j)!=0)
                al.get(i).get(j).setText(Integer.toString(Client.board.getBoardElement(i, j)));
            }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        JLabel a = ((JLabel)me.getSource());
        if(Client.canMove ){
            a.setText(Integer.toString(Client.user_id));
            Point p = (Point) ((a.getClientProperty("Location")));
            
            Client.input = "("+p.x+","+p.y+")";
            Client.button_pressed = true;     
            
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
    
}

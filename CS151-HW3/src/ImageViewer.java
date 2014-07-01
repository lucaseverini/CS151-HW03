/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JComponent;

public class ImageViewer extends JComponent{
    Image currentImage;
    Image getCurrentImage(){
        return currentImage;
    }
    void setCurrentImage(Image i){
        currentImage = i;
    }
    @Override public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(getCurrentImage(), 0,0, this);
        //Coordinates will need to be worked out.
    }
}
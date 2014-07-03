/*
	ImageViewer.java

    Assignment #3 - CS151 - SJSU
	By Luca Severini, Omari Straker, Syed Sarmad, Matt Szikley
	June-30-2014
*/

package SlideShow;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.Box;
import javax.swing.JComponent;

public class ImageViewer extends JComponent
{
	private static final long serialVersionUID = 1L;
    Image currentImage;
    Box myBox;
    
    ImageViewer(Box b){
        myBox = b;
    }
	
    Image getCurrentImage()
	{
        return currentImage;
    }
	
    void setCurrentImage(Image i)
	{
        currentImage = i;
    }
	
    @Override 
	public void paintComponent(Graphics g)
	{
        Graphics2D g2 = (Graphics2D) g;
        if(currentImage!=null)
        g2.drawImage(currentImage, (((int)myBox.getWidth())-currentImage.getWidth(this))/2,
                (((int)myBox.getHeight())-currentImage.getHeight(this))/2,this); 
    }
}

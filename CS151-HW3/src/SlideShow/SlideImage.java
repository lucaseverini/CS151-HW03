/*
	Serializer.java

    Assignment #3 - CS151 - SJSU
	By Luca Severini, Omari Straker, Syed Sarmad, Matt Szikley
	June-24-2014
*/

package SlideShow;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.imageio.ImageIO;

/*
 * The purpose of this class is to represent a Slide in a slide show.
 * @param image, the image in the slide.
 * @param caption, the caption to the image.
 * @param filePath, the path to the image. 
 */
public class SlideImage implements Serializable
{
	private static final long serialVersionUID = 1L;
    private transient BufferedImage image;
    private String caption; 
	private String filePath;
    
   /* 
    * Default Constructor That creates 
    * the SlideImage Object (Slide)
    * @param image, image SlideImage(Slide) holds,
    * @param caption, caption to the image
    * @param filepath, the file path to the image
    */
    public SlideImage() 
    {
        this.image = null;
        this.caption = null;
        this.filePath = null;
    }
/*
	SlideImage.java

    Assignment #3 - CS151 - SJSU
	By Luca Severini, Omari Straker, Syed Sarmad, Matt Szikley
	June-26-2014
*/
    /*
     * Constructor which sets the Image, the file path for that image and
     * caption upon creation. 
     * @param image, image SlideImage(Slide) holds,
     * @param caption, caption to the image
     * @param filepath, the file path to the image
     */
    public SlideImage(String caption, String filePath, BufferedImage image)
    {
        this.caption = caption;
        this.filePath = filePath;
        this.image = image;
    }
       
    /*
     * Returns the Slide's image
     * @param none.
     */
    public BufferedImage getImage()
    {
        return image;
    }
    
    /*
     * Set the image bieng passed in
     * to the SlideImage Object(Slide).
     * @param image, image to be set to slide.
     */
    public void setImage(BufferedImage image)
    {
        this.image = image;
    }
    
    /* 
     * Returns the Slide's Caption;
     * @param none.
     */
    public String getCaption()
    {
        return caption;
    }
    
    /*
     * Sets the string bieng passed in
     * to the SlideImage Object(Slide). 
     * @param caption, caption to be set to the Slide
     */ 
    public void setCaption(String caption)
    {
        this.caption = caption;
    }
    
    /* 
     * Returns the File Path of the image to the Slide
     * @param none.
     */
    public String getFilePath()
    {
        return filePath;
    }
    
    /*
     * Sets the string being passed in
     * to the SlideImage Object (Slide).
     * @param filePath, file path of the image in the Slide
     */
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
            
    }
    
    /*
     * Returns the a which describes the object instance
     * @param none
     */
	@Override
    public String toString()
    {
        if ((caption == null) || (caption.equals("")))
        { 
            return "Untitled";
        }
        else
        {
            return "Image: " + caption;
        }
    }
	
    /*
     * Reads the data from the input stream and converts it back into the object properties
     * @param inputStream, the input stream from which the data is read
     */
	private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException 
	{
		inputStream.defaultReadObject();
		image = ImageIO.read(inputStream);
	}
	
    /*
     * Converts and writes the object properties into output stream
     * @param outputStream, the output stream in which the data is written
     */
   private void writeObject(ObjectOutputStream outputStream) throws IOException 
	{
        outputStream.defaultWriteObject();
		ImageIO.write(image, "png", outputStream);
	}
}

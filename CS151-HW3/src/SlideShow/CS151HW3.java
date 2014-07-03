package SlideShow;


/*
	CS151HW3.java

    Assignment #3 - CS151 - SJSU
	By Luca Severini, Omari Straker, Syed Sarmad, Matt Szikley
	June-21-2014
*/

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CS151HW3
{
	// @param args the command line arguments
	public static void main(String[] args)
	{
		serializerTest();
	}
	
	static void serializerTest()
	{
		SlideShow sShow = new SlideShow();
		
		sShow.setName("Name");
		
		SlideImage slide = new SlideImage();
		
		slide.setCaption("Caption");
		
		try
		{
			BufferedImage image = ImageIO.read(new File("/Users/Luca/Desktop/image.png"));
			
			slide.setImage(image);
		}
		catch(IOException ex)
		{
			System.out.println(ex);
		}
		
		sShow.addSlide(slide);
		
		try
		{
			int result = Serializer.saveSlideToFile(sShow, "/Users/Luca/Desktop/slide_show.xml");
			System.out.println("Result: " + result);
			
			SlideShow readSlideShow = Serializer.openSlideFromFile("/Users/Luca/Desktop/slide_show.xml");
			System.out.println("Result: " + readSlideShow);
		}
		catch(Exception ex)
		{
			System.out.println(ex);
		}
	}
}

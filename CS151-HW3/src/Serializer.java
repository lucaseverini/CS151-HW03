/*
	Serializer.java

    Assignment #3 - CS151 - SJSU
	By Luca Severini, Omari Straker, Syed Sarmad, Matt Szikley
	June-24-2014
*/

//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;

// Serializer -----------------------------------------------------
public class Serializer 
{
	public static int saveSlideToFile(Object obj, String filePath) throws Exception
	{
		if(!(obj instanceof SlideShow))
		{
			throw new Exception();
		}
		
		SlideShow sShow = (SlideShow)obj;
		ArrayList<SlideImage> slides = sShow.getImages();
		
		int idx = 0;
		for(SlideImage slide : slides)
		{
			BufferedImage image = slide.getImage();

			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", outStream);
			
			String imageData = Base64.encodeBase64String(outStream.toByteArray());

			System.out.println("Slide " + ++idx);
			System.out.println(imageData);
			System.out.println(slide.getCaption());
			System.out.println("");
		}
		
		return 0;
	}
	
	public static Object openSlideToFile(String filePath)
	{
		return null;
	}
}

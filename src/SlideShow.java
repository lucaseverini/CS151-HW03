
import java.util.*;

/*
 * Copyright (C) 2014 Sarmad Syed. All Rights reserved.
 */

/**
 *
 * @author Sarmad Syed, Omari Straker, Matt Sziklay, Luca Severini
 */
public class SlideShow 
{
   private static ArrayList<SlideImage> images;
   private static String filePathSlideShow;

    public SlideShow() 
    { 
        this.images = new ArrayList<>();
        this.filePathSlideShow = null;
    }
   
   public static void addSlide(SlideImage newImage)
   {
        images.add(newImage);
   }
   
   public static void removeSlide(SlideImage slide)
   {
        for(int i = 0; i < images.size(); i++)
        {
           if(images.get(i).getCaption().equals(slide.getCaption()))
            {
               images.remove(i);
            }
           
           System.out.println("Slide is not in List");
        }
   }
   
   public static void removeSlides(int index)
   {
       
   }
}

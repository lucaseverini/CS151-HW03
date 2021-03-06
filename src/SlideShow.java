
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
   private ArrayList<SlideImage> images;
   private String filePathSlideShow;

    public SlideShow() 
    { 
        this.images = new ArrayList<>();
        this.filePathSlideShow = null;
    }
   
   public void addSlide(SlideImage newImage)
   {
        this.images.add(newImage);
   }
   
   public void removeSlide(SlideImage slide)
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
   
   public void removeSlides(int index)
   {
       images.remove(index);
   }
   public String getFilePath()
   {
       return filePathSlideShow;
   }
   public void setFilePath(String datPath)
   {
       filePathSlideShow = datPath;
   }
   public Object[] toArray()
   {
      return images.toArray(); 
   }
}

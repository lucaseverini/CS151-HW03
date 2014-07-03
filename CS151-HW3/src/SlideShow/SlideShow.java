/*
	SlideShow.java

    Assignment #3 - CS151 - SJSU
	By Luca Severini, Omari Straker, Syed Sarmad, Matt Szikley
	June-26-2014
*/

package SlideShow;

import java.io.Serializable;
import java.util.*;

/*
 * The purpose of this class is to hold the SlideImage object in 
 * an ArrayList, to create a Slide show 
 * the SlideShow class will also contain a String to contain the file path of
 * the slide show.
 * @param images, the ArrayList of Images
 * @param filePathSlideShow, the path to the SlideShow.
 */
public class SlideShow implements Serializable
{
	private final ArrayList<SlideImage> images;
	private String name;
	private String filePathSlideShow;

	/*
	 * Constructor initializes the ArrayList and sets the file Path
	 * of the SLide Show to null. 
	 * @param none.
	 */
	public SlideShow() 
	{ 
		this.images = new ArrayList<>();
		this.filePathSlideShow = null;
	}

	/*
	 * Add SlideImage Object to the Array list
	 * @param newImage, new SlideImage to be added to ArrayList
	 */   
	public void addSlide(SlideImage newImage)
	{
		this.images.add(newImage);
	}
   
   /*
     * removes the Slide from the ArrayList of SlideImages 
     * by iterating through the ArrayList and Comparing the caption 
     * of the SlideImage with the caption of the SlideImage bieng passed in.
     * if it is equal the slide is removed.
     * @param slide, SlideImage in the ArrayList to be compared against.
     */
	public void removeSlide(SlideImage slide)
	{
		for(int i = 0; i < images.size(); i++)
		{
		   if(images.get(i).getCaption().equals(slide.getCaption()))
			{
			   images.remove(i);
			}
		   //need to deal with this somehow still a work in progress.
		   System.out.println("Slide is not in List");
		}
	}
        
        /*
         * Gets the Current Index of the Slide for Use with removeSlide by index.
         * and getCurrentSlide by index, by iterating through the ArrayList and
         * Comparing each caption to SlideImage bieng passed in.
         * @param slide, SlideImage to compare against.
        */
        public int getCurrentIndex(SlideImage slide)
        {
            for(int i = 0; i < images.size(); i++)
		{
		   if(images.get(i).getCaption().equals(slide.getCaption()))
			{
			   return i;
			}
                }   
            return -1;
        } 

	/*
	 * removes the Slide from the ArrayList of SlideImages 
	 * by using the Index of the Slide.
	 * @param index, index of the slide in the ArrayList
	 */
	public void removeSlides(int index)
	{
	   images.remove(index);
	}
        
        /*
         * Returns the current slide according to its location 
         * in the ArrayList. 
         * @param index, the index of the slide in the ArrayList.
         */
	public SlideImage getCurrentSlide(int index)
	{
	   return images.get(index);
	}

	/*
	 * Sets the string being passed in
	 * to the path for the SlideShow Object
	 * @param datpath, file path of the SlideShow
	 */
	public void setPath(String datpath)
	{
	   this.filePathSlideShow = datpath;
	}

	/* 
	 * Returns the File Path of the SlideShow
	 * @param none.
	 */
	public String getFilePath()
	{
	   return filePathSlideShow;
	}

	 /* 
	 * Returns the Images list of the SlideShow
	 * @param none.
	 */
	public ArrayList<SlideImage> getImages()
	{
	   return images;
	}

	/* 
	 * Returns the name of SlideShow
	 * @param none.
	 */
	public String getName()
	{
	   return name;
	}

	/* 
	 * Set the name of SlideShow
	 * @param String containing the name.
	 */
	public void setName(String theName)
	{
	   name = theName;
	}

	public Object[] toArray()
	{
	   return images.toArray();
	}
	
	@Override
	public String toString()
	{
		return "SlideShow " + name + " : " + getImages().size() + " slide(s)";
	}
}

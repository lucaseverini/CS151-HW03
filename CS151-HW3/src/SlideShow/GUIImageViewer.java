/*
 GUIImageViewer.java

 Assignment #3 - CS151 - SJSU
 By Luca Severini, Omari Straker, Syed Sarmad, Matt Szikley
 June-30-2014
 */
package SlideShow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Arrays;
import javax.swing.BorderFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUIImageViewer {

    static Box myBox = Box.createVerticalBox();
    static Box imageBox = Box.createVerticalBox();
    static JLabel currentCaption = new JLabel("sample caption");
    static JMenuBar menuBar = new JMenuBar();
    static JMenu fileMenu = new JMenu("File");
    static JButton browseButton = new JButton("Browse for Image");
    static JButton saveButton = new JButton("Save Slide");
    static JButton addButton = new JButton("Create Slide");
    static JButton removeButton = new JButton("Remove Slide");
    static JTextArea searchField = new JTextArea(1, 20);
    static JLabel searchLabel = new JLabel("Search: ");
    static JFrame myFrame = new JFrame("Slide Wizard");
    static JPanel pnlRight = new JPanel();
    static JTextArea fileArea = new JTextArea(1, 10);
    static JTextArea captionArea = new JTextArea(1, 10);
    static JMenuItem newMenu = new JMenuItem("New");
    static JMenuItem saveMenu = new JMenuItem("Save");
    static JMenuItem openMenu = new JMenuItem("Open");
    static JMenuItem exitMenu = new JMenuItem("Exit");
    static Object[] slides;
    static JList<SlideImage> slideList;
    static DefaultListModel model;
    static JFileChooser chooser = new JFileChooser();
    static FileNameExtensionFilter picfilter = new FileNameExtensionFilter(
            "JPG, PNG, or BMP", "jpg", "png", "bmp");
    static FileNameExtensionFilter txtfilter = new FileNameExtensionFilter(
            "TXT", "txt");
    static int returnval;
    static SlideShow sshow = new SlideShow();
    static BorderLayout myLayout = new BorderLayout();
    static ImageViewer myViewer = new ImageViewer(imageBox);

    static GUIListener myListener = new GUIListener();

    public static void main(String[] args) {
        menuBar.add(fileMenu);
        fileMenu.add(newMenu);
        fileMenu.add(saveMenu);
        fileMenu.add(openMenu);
        fileMenu.add(exitMenu);
        newMenu.addActionListener(myListener);
        saveMenu.addActionListener(myListener);
        openMenu.addActionListener(myListener);
        exitMenu.addActionListener(myListener);
        myFrame.setLayout(myLayout);
        myFrame.setMinimumSize(new Dimension(900, 600));
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setJMenuBar(menuBar);
        Box browseBox = Box.createHorizontalBox();
        //browseBox.add(new JLabel("Image: "));
        //browseBox.add(fileArea);
        browseButton.addActionListener(myListener);
        saveButton.addActionListener(myListener);
        addButton.addActionListener(myListener);
        removeButton.addActionListener(myListener);
        addButton.setSize(new Dimension(200, 50));
        browseButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        browseBox.add(browseButton);
        myBox.add(Box.createRigidArea(new Dimension(0, 10)));
        myBox.add(browseBox);
        myBox.add(Box.createRigidArea(new Dimension(0, 10)));
        Box captionBox = Box.createHorizontalBox();
        captionBox.add(Box.createRigidArea(new Dimension(35, 0)));
        captionBox.add(new JLabel("Caption:"));
        captionBox.add(Box.createRigidArea(new Dimension(10, 0)));
        captionBox.add(captionArea);
        captionBox.add(Box.createRigidArea(new Dimension(40, 0)));
        myBox.add(captionBox);
        myBox.add(Box.createRigidArea(new Dimension(0, 10)));
        Dimension listSize = new Dimension(180, 200);
        /*try {
         TestCode();
         } catch (IOException ex) {
         Logger.getLogger(GUIImageViewer.class.getName()).log(Level.SEVERE, null, ex);
         System.out.println("Test file not found.");
         }*/
        slideList = new JList(slides);
        slideList.setMaximumSize(listSize);
        slideList.setMinimumSize(listSize);
        slideList.addListSelectionListener(new JListListener());
        myBox.add(slideList);
        myBox.add(Box.createRigidArea(new Dimension(0, 10)));
        saveButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        addButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        removeButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        myBox.add(saveButton);
        myBox.add(Box.createRigidArea(new Dimension(0, 10)));
        myBox.add(addButton);
        myBox.add(Box.createRigidArea(new Dimension(0, 10)));
        myBox.add(removeButton);
        myFrame.getContentPane().add(myBox, myLayout.WEST);
        currentCaption.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        currentCaption.setFont(currentCaption.getFont().deriveFont(20.0f));
        currentCaption.setOpaque(true);
        imageBox.add(myViewer);
        imageBox.add(Box.createRigidArea(new Dimension(0,10)));
        imageBox.add(currentCaption);
        imageBox.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.gray));
        myViewer.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.gray));
        imageBox.add(Box.createRigidArea(new Dimension(0, 10)));
        myFrame.getContentPane().add(imageBox, myLayout.CENTER);
        Dimension browseSize = new Dimension(80, 20);
        fileArea.setMinimumSize(browseSize);
        fileArea.setMaximumSize(browseSize);
        Dimension captionSize = new Dimension(150, 20);
        captionArea.setMaximumSize(captionSize);
        captionArea.setMaximumSize(captionSize);
        createNewSlideShow();
        myFrame.setVisible(true);

    }

    public static class GUIListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == browseButton) {
                browseForImage();
            }

            if (event.getSource() == saveButton) {
                saveSlide();
            }

            if (event.getSource() == addButton) {
                addNewSlide();
            }

            if (event.getSource() == removeButton) {
                removeSlide();
            }

            if (event.getSource() == newMenu) {
                // Clear the JList, clear the current slideshow
                createNewSlideShow();
            }

            if (event.getSource() == saveMenu) {
                saveSlideShow();
            }

            if (event.getSource() == openMenu) {
                openSlideShow();
            }

            if (event.getSource() == exitMenu) {
                myFrame.dispose();
            }
        }
    }

    public static class JListListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            refreshSlide();
        }
    }

    public static void createNewSlideShow() {
        sshow = new SlideShow();
        addNewSlide();

    }

    public static void saveSlideShow() {
        File currFile;
        String currPath = sshow.getFilePath();
        if ((currPath == null) | (currPath.equals(""))) {
            currFile = Browse(false, txtfilter);
        } else {
            currFile = new File(currPath);
        }
        //TODO: add try/catch block inside else statement, and actually save file info
        //Luca
    }

    public static void openSlideShow() {
        File currFile = Browse(true, txtfilter);
        //Luca
    }

    public static void addNewSlide() {
        sshow.addSlide(new SlideImage());
        refreshJLIst();
        refreshSlide();
        slideList.setSelectedIndex(sshow.getSize() - 1);
    }

    public static void saveSlide() //???
    {
        slideList.getSelectedValue().setCaption((captionArea.getText()));
        refreshJLIst();
    }

    public static void removeSlide() {
        //only try to remove if a row is selected to prevent exception//
        if (slideList.getSelectedIndex() != -1) {
            sshow.removeSlides(slideList.getSelectedIndex());
            if (sshow.getSize() == 0) {
                addNewSlide();
            } else {
                if (slideList.getSelectedIndex() >= sshow.getSize()) {
                    slideList.setSelectedIndex(sshow.getSize() - 1);
                }
            }
        }

        refreshJLIst();
        refreshSlide();
        //Sarmad
    }

    public static void refreshSlide() {
        //in case no row is actually selected, don't want to cause a runtime error. 
        //Just auto select the first row on the jList because list will never be empty
        if (slideList.getSelectedIndex() == -1) {
            slideList.setSelectedIndex(0);
        }
        String display = slideList.getSelectedValue().toString();
        display = display.replace("Image: ", "");
        captionArea.setText("" + display);
        currentCaption.setText("" + display);
        myViewer.setCurrentImage(slideList.getSelectedValue().getImage());
        myViewer.repaint();
        //Omari   
    }

    public static File Browse(boolean opentruesavefalse, FileNameExtensionFilter filter) { //Omari
        int result;

        chooser.resetChoosableFileFilters();
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);

        if (opentruesavefalse) {
            result = chooser.showOpenDialog(null);
        } else {
            result = chooser.showSaveDialog(null);
        }

        if (result == JFileChooser.APPROVE_OPTION) {
            File currFile = chooser.getSelectedFile();
            chooser.setSelectedFile(null);
            return currFile;
        }

        if (result == JFileChooser.ERROR_OPTION) {
            //TODO: Error message
        }

        return null;
    }

    // Update Object Array and reconstruct JList with new array
    public static void refreshJLIst() {
        int lol = slideList.getSelectedIndex();
        slides = sshow.toArray();
        //slideList = new JList(slides);
        //SlideImage[] yaya = (SlideImage[])slides;
        SlideImage[] slImageArray = Arrays.copyOf(slides, slides.length, SlideImage[].class);
        slideList.setListData(slImageArray);
        slideList.setSelectedIndex(lol);
    }

    // call browse to get the file, if a file is found and the row is highlighted,
    public static void browseForImage() {
        //assign that image to Slide Image instance  
        File currFile = Browse(true, picfilter);
        try {
            if (currFile != null) {
                if (slideList.getSelectedIndex() != -1) {
                    slideList.getSelectedValue().setImage(ImageIO.read(currFile));
                }
            }
        } catch (Exception e) {
            System.out.println("Image file could not be added: " + e.getMessage());
            // TODO: use messagen box
        }
        refreshSlide();
    }

    //SlideImages part of test method. To be deleted later.
    /*public static void TestCode() throws IOException
     {   //Test method to be used for easily creating test data in one place for quick removal  
     //TODO: Remove method
     cat = new SlideImage("cat", "cat.jpg", ImageIO.read(new File("Test_Files/cat.jpg")));
     dog = new SlideImage("dog", "dog.jpg", ImageIO.read(new File("Test_Files/dog.jpg")));
     chicken = new SlideImage("chicken", "chicken.jpg", ImageIO.read(new File("Test_Files/chicken.jpg")));
     sshow.addSlide(cat);
     sshow.addSlide(dog);
     sshow.addSlide(chicken);
     sshow.addSlide(new SlideImage());
     slides = sshow.toArray(); 
     }*/
}

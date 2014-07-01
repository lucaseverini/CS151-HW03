

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUIImageViewer {
    static Box myBox = Box.createVerticalBox();
    static Box imageBox = Box.createVerticalBox();
    static BufferedImage currentImage = new BufferedImage(400,400,BufferedImage.TYPE_INT_RGB);
    static ImageIcon currentImageIcon = new ImageIcon(currentImage);
    static JLabel currentCaption = new JLabel("sample caption");
    static JMenuBar menuBar = new JMenuBar();
    static JMenu fileMenu = new JMenu("File");
    static JButton forwardButton = new JButton("Search Forward");
    static JButton backButton = new JButton("Search Backward");
    static JButton browseButton = new JButton("Browse");
    static JButton saveButton = new JButton("Save");
    static JButton addButton = new JButton("Add New");
    static JButton removeButton = new JButton("Remove Slide");
    static JTextArea searchField = new JTextArea(1,20);
    static JLabel searchLabel = new JLabel("Search: ");
    static JFrame myFrame = new JFrame("Image Viewer");
    static JPanel pnlRight = new JPanel();
    static JTextArea text = new JTextArea("Image and caption will go here.", 5,5);
    static JScrollPane scrollText = new JScrollPane(text);
    static JTextArea fileArea = new JTextArea(1,10);
    static JTextArea captionArea = new JTextArea(1,10);
    static JMenuItem newMenu = new JMenuItem("New");
    static JMenuItem saveMenu = new JMenuItem("Save");
    static JMenuItem openMenu = new JMenuItem("Open");
    static JMenuItem exitMenu = new JMenuItem("Exit");
    static Object[] slides;
    static JList<SlideImage> slideList;
    static DefaultListModel model;
    static JFileChooser chooser = new JFileChooser();
    static FileNameExtensionFilter picfilter = new FileNameExtensionFilter(
            "JPG, PNG, or BMP", "jpg","png","bmp");
    static FileNameExtensionFilter txtfilter = new FileNameExtensionFilter(
           "TXT", "txt");
    static int returnval;
    static SlideShow sshow = new SlideShow();
    static BorderLayout myLayout = new BorderLayout();
    static ImageViewer myViewer = new ImageViewer();
    static SlideImage cat,dog,chicken;
    //Part of test method. To be deleted later.
    public static void TestCode() throws IOException
    {   //Test method to be used for easily creating test data in one place for quick removal  
        //TODO: Remove method
        cat = new SlideImage("cat","cat.jpg", ImageIO.read(new File("cat.jpg")));
        dog = new SlideImage("dog","dog.jpg", ImageIO.read(new File("dog.jpg")));
        chicken = new SlideImage("chicken","chicken.jpg", ImageIO.read(new File("chicken.jpg")));
        sshow.addSlide(cat);
        sshow.addSlide(dog);
        sshow.addSlide(chicken);
        sshow.addSlide(new SlideImage());
        slides = sshow.toArray(); 
    }
    public static void main(String[] args) {
        menuBar.add(fileMenu);
        fileMenu.add(newMenu);
        fileMenu.add(saveMenu);
        fileMenu.add(openMenu);
        fileMenu.add(exitMenu);
        newMenu.addActionListener(new GUIListener());
        saveMenu.addActionListener(new GUIListener());
        openMenu.addActionListener(new GUIListener());
        exitMenu.addActionListener(new GUIListener());
        myFrame.setLayout(myLayout);
        myFrame.setMinimumSize(new Dimension(900,600));
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setJMenuBar(menuBar);
        Box browseBox = Box.createHorizontalBox();
        browseBox.add(new JLabel("Image:"));
        browseBox.add(fileArea);
        browseButton.addActionListener(new GUIListener());
        saveButton.addActionListener(new GUIListener());
        addButton.addActionListener(new GUIListener());
        removeButton.addActionListener(new GUIListener());
        browseBox.add(browseButton);
        myBox.add(browseBox);
        myBox.add(Box.createRigidArea(new Dimension(0,10)));
        Box captionBox = Box.createHorizontalBox();
        captionBox.add(new JLabel("Caption:"));
        captionBox.add(captionArea);
        myBox.add(captionBox);
        myBox.add(Box.createRigidArea(new Dimension(0,10)));
        myBox.add(saveButton);
        myBox.add(Box.createRigidArea(new Dimension(0,10)));
        Dimension listSize = new Dimension(180,200);
        try {
            TestCode();
        } catch (IOException ex) {
            Logger.getLogger(GUIImageViewer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Test file not found.");
        }
        slideList = new JList(slides);
        slideList.setMaximumSize(listSize);
        slideList.setMinimumSize(listSize);
        slideList.addListSelectionListener(new JListListener());
        myBox.add(slideList);
        myBox.add(Box.createRigidArea(new Dimension(0,10)));
        myBox.add(addButton);
        myBox.add(Box.createRigidArea(new Dimension(0,10)));
        myBox.add(removeButton);
        myFrame.getContentPane().add(myBox,myLayout.WEST);
        //Above code returns an error. Couldn't quite get
        //The image area squared off right
        imageBox.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        imageBox.add(myViewer);
        imageBox.add(currentCaption);
        myFrame.getContentPane().add(imageBox,myLayout.CENTER);
        Dimension browseSize = new Dimension(80,20);
        fileArea.setMinimumSize(browseSize);
        fileArea.setMaximumSize(browseSize);
        Dimension captionSize = new Dimension(150,20);
        captionArea.setMaximumSize(captionSize);
        captionArea.setMaximumSize(captionSize);
        myFrame.setVisible(true);
        
    }
    public static class GUIListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if (event.getSource() == browseButton)
                Browse(true,picfilter);
            if (event.getSource() == saveButton){
                returnval = chooser.showOpenDialog(null);
            }
            if (event.getSource() == addButton){
                
            }
            if (event.getSource() == removeButton){
                removeSlide();
            }
            if (event.getSource() == newMenu)
                //Clear the JList, clear the current slideshow
                createNewSlideShow();
            if (event.getSource() == saveMenu){
                saveSlideShow();
            } 
            if (event.getSource() == openMenu){
                openSlideShow();
            }
            if (event.getSource() == exitMenu)
                myFrame.dispose();
        }
    }
    
    public static class JListListener implements ListSelectionListener
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                
                captionArea.setText(""+ slideList.getSelectedValue());
                currentCaption.setText(""+ slideList.getSelectedValue());
                myViewer.setCurrentImage(slideList.getSelectedValue().getImage());
                myViewer.repaint();
               
            }
        }
    
    public static void createNewSlideShow()
    { 
          sshow = new SlideShow();
          addNewSlide();
    }
    
    public static void saveSlideShow()
    {
        File currFile;
        String currPath = sshow.getFilePath();
        if ((currPath.equals(""))|(currPath == null))
        {
            currFile = Browse(false, txtfilter);
        }
        else
        {
            currFile = new File(currPath);
        }
        //TODO: add try/catch block inside else statement, and actually save file info
        //Luca
    }
    
    public static void openSlideShow()
    {
        File currFile = Browse(true, txtfilter);
        //Luca
    }
    
    public static void addNewSlide()
    {
        sshow.addSlide(new SlideImage());
    }
    
    public static void saveSlide() //???
    {
        slideList.getSelectedValue().setCaption((captionArea.getText()));
    }
    
    public static void removeSlide()
    {
        sshow.removeSlides(slideList.getSelectedIndex());
        //Sarmad
        
    }
    
    public static void refreshSlide()
    {
     //Omari   
    }
    
    public static void replaceSlide()
    {
        //Possibly axed?
    }
    
    public static void OnExit()
    {
        
    }
    
    public static File Browse(boolean opentruesavefalse, FileNameExtensionFilter filter)
    { //Omari
        int result;
        chooser.resetChoosableFileFilters();
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        if (opentruesavefalse)
        {
            result = chooser.showOpenDialog(null);
        }
        else
        {
            result = chooser.showSaveDialog(null);
        }
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File currFile = chooser.getSelectedFile();
            chooser.setSelectedFile(null);
            return currFile;
        }
        if (result == JFileChooser.ERROR_OPTION)
        {
           //TODO: Error message
        }
        return null;
    }
    }
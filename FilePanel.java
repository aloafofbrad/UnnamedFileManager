import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author Bradley Nickle
 */
public class FilePanel extends JPanel{
    // Fields that will represent the file. Declared & instantiated in order from left to right.
    private JLabel pic,filename,size,dateCreated,dateModified;
    // An image that will be tied to this.pic.
    private ImageIcon icon;
    // Tracks whether or not this file should be selected.
    private boolean isSelected,isDirectory;
    private String absolutePath;
    
    /**
     * Default constructor for FilePanels.
     * @param fn the filename (relative path, more or less)
     * @param dp the DirectoryPanel which will be the MouseListener for the
     *           FilePanel and its components
     */
    public FilePanel(String fn,DirectoryPanel dp){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        
        /* Configure the components for all fields relevant to the file represented
        by this FilePanel */
        
        // Configure the icon
        icon = new ImageIcon("src/main/java/icons/file.png",fn);
        pic = new JLabel(icon);
        pic.addMouseListener(dp);
        add(pic);
        
        // Configure name
        filename = new JLabel(fn);
        filename.addMouseListener(dp);
        filename.setToolTipText(fn);
        add(filename);
        
        // Configure size
        size = new JLabel("null");
        size.setToolTipText("Size in bytes");
        size.addMouseListener(dp);
        add(size);
        
        // Configure date created
        dateCreated = new JLabel("null");
        dateCreated.addMouseListener(dp);
        dateCreated.setToolTipText("Date Created");
        add(dateCreated);
        
        // Configure date modified
        dateModified = new JLabel("null");
        dateModified.addMouseListener(dp);
        dateModified.setToolTipText("Date Modified");
        add(dateModified);
        
        // Configure the file, filling in the fields with appropriate names and dates
        absolutePath = dp.getCurrentPath();
        configureFile(absolutePath);
        
        // Add a MouseListener & deselect this
        addMouseListener(dp);
        select(false);
    }
    
    /**
     * Configures the field components with the file's data.
     * @author Bradley Nickle
     * @author Dan Tran
     */
    private void configureFile(String path){        
        // Make sure that the path is properly delimited by "\\"
        int last = path.length() - 1;
        char delimiter = '\\';
        if (path.charAt(last) != delimiter){
            path = path + "\\";
        }
        
        /* self is the File represented by the FilePanel. Not to be confused with
        the Python naming convention in which "self" is analogous to "this". */
        File self = new File(path + this.filename.getText());
        
        // Update the file data based on file type (directory or otherwise)
        this.isDirectory = self.isDirectory();
        if (this.isDirectory){
            // Update the icon
            this.icon = new ImageIcon("src/main/java/icons/folder.png",path);
            
            // Hide the size of a directory.
            this.size.setVisible(false);
        }
        else{
            // Update the icon
            this.icon = new ImageIcon("src/main/java/icons/file.png",path);
            // Show & set text for fields based on file data. Credit to Dan Tran
            this.size.setText(Long.toString(self.length()));
            this.size.setVisible(true);
        }
        // Update the icon
        this.pic.setIcon(this.icon);
        
        // Update the date modified
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
        try{
            this.dateModified.setText(sdf.format(self.lastModified()));
        }
        catch (SecurityException e){
            System.out.println(e.getMessage());
            this.dateModified.setText("01/01/1970 00:00");
        }
        
        // TODO Update the date created
        //dateCreated.setText(sdf.format(self.))
    }
    
    /**
     * @return the name of the file
     */
    public String getFileName(){
        return filename.getText();
    }
    
    /**
     * @return the name of the file
     */
    public String getFullFileName(){
        return absolutePath + filename.getText();
    }
    
    /**
     * @return the directory the file is stored in 
     */
    public String getAbsolutePath(){
        return absolutePath;
    }
    
    /**
     * @return the size of the file in bytes
     */
    public int getFileSize(){
        return Integer.getInteger(size.getText());
    }
    
    // TODO insert Ian's code here
    /**
     * 
     * @param b 
     */
    public void select(boolean b){
        isSelected = b;
        if (isSelected){
            setBackground(new Color(127,127,255));
        } else{
            setBackground(Color.white);
        }
    }
    
    /**
     * 
     * @return a boolean representing whether or not this is selected
     */
    public boolean isSelected(){
        return isSelected;
    }
    
    /**
     * 
     * @param o the event Object to be compared with
     * @return whether or not this FilePanel has (or is) a component equal to o
     */
    public boolean hasSource(Object o){
        if (o == this) return true;
        if (o == filename) return true;
        if (o == dateCreated) return true;
        if (o == dateModified) return true;
        if (o == pic) return true;
        if (o == size) return true;
        return false;
    }

    // TODO determine whether or not anything from here is still needed
    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     * @author Bradley Nickle
     * @author Dan Tran
     */
    /*@Override
    public void mouseClicked(MouseEvent e) {
        // Get the number of clicks. Credit to Dan Tran
        final int CLICKS = e.getClickCount();
        System.out.print(CLICKS + " ");
        
        if (e.getSource() == this ||
            e.getSource() == pic ||
            e.getSource() == filename ||
            e.getSource() == size ||
            e.getSource() == dateCreated ||
            e.getSource() == dateModified)
        {
            if (CLICKS == 2)
            {
                System.out.println("Double Click");
            }
            else if (CLICKS == 1) // Single clicks.
            {
                System.out.println("Single Click");
                select(!isSelected);
            }
            else if (e.getButton() == MouseEvent.BUTTON3)
            {
                System.out.println("Right Click");
            }
        }
    }*/
}

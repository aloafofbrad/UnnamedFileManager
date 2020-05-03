import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.attribute.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    // Layout manager & layout constants
    private SpringLayout layout;
    private final int[] HORIZONTAL_GAPS = {24,256,72,128};
    private final int VERTICAL_GAP = 4;
    
    /**
     * Default constructor for FilePanels.
     * @param fn the filename (relative path, more or less)
     * @param dp the DirectoryPanel which will be the MouseListener for the
     *           FilePanel and its components
     */
    public FilePanel(String fn,DirectoryPanel dp){
        int sum = 4;
        layout = new SpringLayout();
        setLayout(layout);
        
        /* Configure the components for all fields relevant to the file represented
        by this FilePanel */
        
        // Configure the icon
        icon = new ImageIcon("src/main/java/icons/file.png",fn);
        pic = new JLabel(icon);
        pic.addMouseListener(dp);
        layout.putConstraint(SpringLayout.WEST,pic,sum,SpringLayout.WEST,this);
        layout.putConstraint(SpringLayout.NORTH,pic,VERTICAL_GAP,SpringLayout.NORTH,this);
        sum += HORIZONTAL_GAPS[0];
        add(pic);
        
        // Configure name
        filename = new JLabel(fn);
        filename.addMouseListener(dp);
        filename.setToolTipText(fn);
        layout.putConstraint(SpringLayout.WEST,filename,sum,SpringLayout.WEST,this);
        layout.putConstraint(SpringLayout.NORTH,filename,VERTICAL_GAP,SpringLayout.NORTH,this);
        sum += HORIZONTAL_GAPS[1];
        add(filename);
        
        // Configure size
        size = new JLabel("null");
        size.setToolTipText("Size in bytes");
        size.addMouseListener(dp);
        layout.putConstraint(SpringLayout.WEST,size,sum,SpringLayout.WEST,this);
        layout.putConstraint(SpringLayout.NORTH,size,VERTICAL_GAP,SpringLayout.NORTH,this);
        sum += HORIZONTAL_GAPS[2];
        add(size);
        
        // Configure date created
        dateCreated = new JLabel("null");
        dateCreated.addMouseListener(dp);
        dateCreated.setToolTipText("Date Created");
        layout.putConstraint(SpringLayout.WEST,dateCreated,sum,SpringLayout.WEST,this);
        layout.putConstraint(SpringLayout.NORTH,dateCreated,VERTICAL_GAP,SpringLayout.NORTH,this);
        sum += HORIZONTAL_GAPS[3];
        add(dateCreated);
        
        // Configure date modified
        dateModified = new JLabel("null");
        dateModified.addMouseListener(dp);
        dateModified.setToolTipText("Date Modified");
        layout.putConstraint(SpringLayout.WEST,dateModified,sum,SpringLayout.WEST,this);
        layout.putConstraint(SpringLayout.NORTH,dateModified,VERTICAL_GAP,SpringLayout.NORTH,this);
        add(dateModified);
        
        this.setPreferredSize(new Dimension((int)(sum * 1.5),25));
        
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
        // Make sure that the path is properly delimited by "/" or "\\"
        int last = path.length() - 1;
        char delimiter = '\\';
        if (path.charAt(last) != delimiter && path.charAt(last) != '\\'){
            path = path + "/";
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
            this.configureSize(self.length());
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
            this.dateModified.setText("null");
        }
        
        // TODO Update the date created
        //dateCreated.setText(sdf.format(self.))
        BasicFileAttributes attrib;
        try{
            attrib = Files.readAttributes(self.toPath(),BasicFileAttributes.class);
            FileTime created = attrib.creationTime();
            this.dateCreated.setText(sdf.format(new Date(created.toMillis())));
        }
        catch(IOException e){
            System.out.println(e.getMessage());
            this.dateCreated.setText("null");
        }
    }
    
    /**
     * Adjusts the displayed file size to a user-friendly approximation.
     * Instead of "1234", it displays "1.234 kb".
     * Also adjusts the tooltip of this.size
     * @author Dan Tran
     * @author Bradley Nickle
     * @param length 
     */
    public void configureSize(Long length){
        // If it's a directory, we don't need to do any work.
        if (!isDirectory){
            BigDecimal filesize = new BigDecimal(length);
            filesize.setScale(3);
            String theSize = length.toString();
            BigDecimal k = new BigDecimal("1000");
            BigDecimal M = new BigDecimal("1000000");
            BigDecimal G = new BigDecimal("1000000000");
            BigDecimal T = new BigDecimal("1000000000000");
            
            // If the file's less than a kilobyte, represent it in bytes.
            if (filesize.compareTo(k) == -1){
                theSize += " B";
                this.size.setToolTipText(filesize.toString() + " bytes");
            }
            // If the file's less than a megabyte, represent it in kilobytes.
            else if (filesize.compareTo(M) == -1){
                filesize = filesize.divide(k);
                filesize.setScale(3);
                theSize = filesize.toString();
                theSize += " KB";
                this.size.setToolTipText(filesize.toString() + " kilobytes");
            }
            // If the file's less than a gigabyte, represent it in megabytes.
            else if (filesize.compareTo(G) == -1){
                filesize = filesize.divide(M);
                filesize.setScale(3);
                theSize = filesize.toString();
                theSize += " MB";
                this.size.setToolTipText(filesize.toString() + " megabytes");
            }
            // If the file's less than a terabyte, represent it in gigabytes.
            else if (filesize.compareTo(T) == -1){
                filesize = filesize.divide(G);
                filesize.setScale(3);
                theSize = filesize.toString();
                theSize += " GB";
                this.size.setToolTipText(filesize.toString() + " gigabytes");
            }
            // If the file's 1 or more terabytes, represent it in terabytes.
            else {
                filesize = filesize.divide(T);
                filesize.setScale(3);
                theSize = filesize.toString();
                theSize += " TB";
                this.size.setToolTipText(filesize.toString() + " terabytes");
            }
            this.size.setText(theSize);
        }
    }
    
    /**
     * @return the name of the file
     */
    public String getFileName(){
        return filename.getText();
    }

    public JLabel getFileNameLabel(){
        return filename;
    }
    
    /**
     * @return the name of the file
     */
    public String getFullFileName(){
        return absolutePath + "/" + filename.getText();
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
     * @author Ian Ho-Sing-Loy
     * @param s New string for the filename
     */
    public void setText(String s){
        filename.setText(s);
    }

    /**
     * 
     * @param b 
     */
    public void select(boolean b){
        isSelected = b;
        if (isSelected){
            setBackground(new Color(100,100,228));
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
     * @return true if the represented file is a directory, false otherwise
     */
    public boolean isDirectory(){
        return isDirectory;
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
}
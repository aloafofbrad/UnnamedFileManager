import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author Bradley Nickle
 */
public class FilePanel extends JPanel implements MouseListener{
    // Fields that will represent the file. Declared & instantiated in order from left to right.
    private JLabel pic,filename,size,dateCreated,dateModified;
    // An image that will be tied to this.pic.
    private ImageIcon icon;
    // Tracks whether or not this file should be selected.
    private boolean isSelected;
    
    /**
     * Default constructor for FilePanels.
     * @param fn 
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
        size = new JLabel("0");
        size.setToolTipText("Size in bytes");
        size.addMouseListener(dp);
        add(size);
        
        // Configure date created
        dateCreated = new JLabel("1/1/1970 00:00");
        dateCreated.addMouseListener(dp);
        dateCreated.setToolTipText("Date Created");
        add(dateCreated);
        
        // Configure date modified
        dateModified = new JLabel("1/1/1970 00:00");
        dateModified.addMouseListener(dp);
        dateModified.setToolTipText("Date Modified");
        add(dateModified);
        
        // Configure the file, filling in the fields with appropriate names and dates
        configureFile();
        
        // Add a MouseListener & deselect this
        addMouseListener(dp);
        select(false);
    }
    
    /**
     * Configures the field components with the file's data.
     * @author Bradley Nickle
     * @author Dan Tran
     */
    private void configureFile(){
        /* self is the File represented by the FilePanel. Not to be confused with
        the Python naming convention in which "self" is analogous to "this". */
        File self = new File(filename.getText());
        
        // Set text for fields based on file data. Credit to Dan Tran
        size.setText(Long.toString(self.length()));
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
        //dateCreated.setText(sdf.format(self.))
        try{
            dateModified.setText(sdf.format(self.lastModified()));
        }
        catch (SecurityException e){
            System.out.println(e.getMessage());
            dateModified.setText("01/01/1970 00:00");
        }
        
    }
    
    /**
     * 
     * @return 
     */
    public String getFileName(){
        return filename.getText();
    }
    
    /**
     * 
     * @param b 
     */
    public void select(boolean b){
        isSelected = b;
        if (isSelected){
            setBackground(Color.blue);
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

    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     * @author Bradley Nickle
     * @author Dan Tran
     */
    @Override
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
    }

    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
}

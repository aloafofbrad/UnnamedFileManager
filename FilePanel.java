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
    public FilePanel(String fn){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        
        /* Configure the components for all fields relevant to the file represented
        by this FilePanel */
        
        // Configure the icon
        icon = new ImageIcon("src/main/java/icons/file.png",fn);
        pic = new JLabel(icon);
        pic.addMouseListener(this);
        add(pic);
        
        // Configure name
        filename = new JLabel(fn);
        filename.addMouseListener(this);
        filename.setToolTipText(fn);
        add(filename);
        
        // Configure size
        size = new JLabel("0");
        size.setToolTipText("Size in bytes");
        size.addMouseListener(this);
        add(size);
        
        // Configure date created
        dateCreated = new JLabel("1/1/1970 00:00");
        dateCreated.addMouseListener(this);
        dateCreated.setToolTipText("Date Created");
        add(dateCreated);
        
        // Configure date modified
        dateModified = new JLabel("1/1/1970 00:00");
        dateModified.addMouseListener(this);
        dateModified.setToolTipText("Date Modified");
        add(dateModified);
        
        // Configure the file, filling in the fields with appropriate names and dates
        configureFile();
        
        // Add a MouseListener & deselect this
        addMouseListener(this);
        select(false);
    }
    
    private void configureFile(){
        File self = new File(filename.getText());
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
     * @return 
     */
    public boolean isSelected(){
        return isSelected;
    }

    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        select(!isSelected);
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

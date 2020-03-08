import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Modified JPanel that will display the contents of a directory (its files and
 * subdirectories).
 * @author Bradley Nickle
 */
public class DirectoryPanel extends JPanel implements MouseListener {
    // File path of the current directory; this is the one that will be displayed
    private String currentDirectory;
    // Contents of the current directory
    private FilePanel[] list;
    /* SpringLayout is the one we want for this one. Define the positions of
    components with respect to the positions of other components. This is important
    for the vertical axis, but not so much for the horizontal axis. GridBagLayout
    and GridLayout both failed to fit our desire to emulate Windows File Explorer's
    display of folder contents. */
    private SpringLayout layout;
    // Visual size of this. NOTE might remove; not sure.
    private Dimension size;
    /* Vertical gap that exists between the top of one FilePanel and the top of
    another, measured in pixels. There's no horizontal equivalent to this since
    we're only currently displaying files in a vertical list view. */
    private final int VERTICAL_FP_GAP = 25;
    
    /**
     * Default constructor for DirectoryPanels.
     * @param current sets this.currentDirectory
     * @param files the contents of the current directory. NOTE might be removed.
     */
    public DirectoryPanel(String current,String[] files){
        currentDirectory = current;
        list = new FilePanel[files.length];
        layout = new SpringLayout();
        setLayout(layout);
        int i;
        for (i = 0;i < files.length;i++){
            list[i] = new FilePanel(files[i]);
            layout.putConstraint(SpringLayout.WEST, list[i], 5, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.NORTH, list[i], i*VERTICAL_FP_GAP, SpringLayout.NORTH, this);
            this.add(list[i]);
        }
        this.addMouseListener(this);
        /* Configure the DirectoryPanel to be scrollable by setting the size and
        calling setAutoscrolls(). */
        size = new Dimension(300,files.length*VERTICAL_FP_GAP);
        this.setPreferredSize(size);
        this.setAutoscrolls(true);
        
        // Set the background color.
        setBackground(Color.white);
        
        // Set a border
        setBorder(BorderFactory.createLineBorder(new Color(128,128,128)));
    }
    
    /**
     * 
     * @return the size of the panel
     */
    @Override
    public Dimension getPreferredSize(){
        return size;
    }

    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
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

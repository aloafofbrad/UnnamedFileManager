import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

/**
 * Modified JPanel that will display the contents of a directory (its files and
 * subdirectories).
 * @author Bradley Nickle
 */
public class DirectoryPanel extends JPanel implements MouseListener,NavigatorObserver {
    // File path of the current directory; this is the one that will be displayed
    private String currentPath;
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
    /**/
    private Navigator fileNavigator;
    
    /**
     * Default constructor for DirectoryPanels.
     * @param n sets this.currentDirectory
     * @author Bradley Nickle
     * @author Dan Tran
     */
    public DirectoryPanel(Navigator n){
        // Configure & observe fileNavigator
        fileNavigator = n;
        fileNavigator.attach(this);
        
        // Get a list of the current directory's contents
        currentPath = fileNavigator.getDirectory();
        File currentDirectory = new File(currentPath);
        String[] files = currentDirectory.list();
        list = new FilePanel[files.length];
        
        // Set up the DirectoryPanel's GUI components
        layout = new SpringLayout();
        setLayout(layout);
        int i;
        for (i = 0;i < files.length;i++){
            list[i] = new FilePanel(files[i],this);
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
     * @return the directory currently being represented by the DirectoryPanel
     */
    public String getCurrentPath(){
        return currentPath;
    }
    
    /**
     * @return the size of the panel
     */
    @Override
    public Dimension getPreferredSize(){
        return size;
    }
    
    /**
     * Checks each FilePanel in a DirectoryPanel's list to see if it was the 
     * source of an event.
     * @param o the source.
     * @return the index of the source FilePanel, or -1 if the source was the
     *         DirectoryPanel, or -999 if it was neither.
     * @author Bradley Nickle
     */
    public int findSource(Object o){
        final int NOSOURCEFOUND = -999;
        if (this == o){
            return -1;
        }
        for (int i = 0;i < list.length;i++){
            if (list[i].hasSource(o)){
                return i;
            }
        }
        return NOSOURCEFOUND;
    }
    
    // TODO find a place for Brandon's code


    /**
     * @param s string to be processed
     * @author Ian Ho-Sing-Loy
     * @return Whether a string contains illegal characters
     * */
    public boolean validString(String s){
        char forbidden[] = {'/', '\\', '?', '*', '"', '<', '>', '|'};

        for(int i = 0; i < s.length(); i++){
            for(int j = 0; j < 8; j++){
                if(s.charAt(i) == forbidden[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     * @author Bradley Nickle
     * @author Dan Tran
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        /* First, discern what was clicked on. If nothing was clicked, nothing
        needs to happen. */
        int sourceIndex = findSource(e.getSource());
        if (sourceIndex != -999){
            // Get the number of clicks. Credit to Dan Tran
            final int CLICKS = e.getClickCount();
            final int BUTTON = e.getButton();
            
            // Double click (with left mouse button)
            if (CLICKS == 2 && BUTTON == java.awt.event.MouseEvent.BUTTON1){
                /* If the double click was on the DirectoryPanel, deselect all
                FilePanels. The following condition is equivalent to:
                 "if (the e.getSource() == this)"
                */
                if (sourceIndex == -1){
                    for (int i = 0;i < list.length;i++){
                        list[i].select(false);
                    }
                }
                
                /* If the double click was on a FilePanel, open the file. */
                else{
                    // TODO open a directory
                    // TODO open a file
                }
            }
            
            // Single left click
            else if (CLICKS == 1 && BUTTON == java.awt.event.MouseEvent.BUTTON1){
                /* If this was any kind of click on the DirectoryPanel, deselect
                all FilePanels. */
                if (sourceIndex == -1){
                    for (int i = 0;i < list.length;i++){
                        list[i].select(false);
                    }
                }

                // TODO rename file
                //  if file pane was initially selected and the filename was single clicked on the second time, rename
                System.out.println(e.getSource());
                System.out.println(list[sourceIndex].getFileNameLabel());
                if(list[sourceIndex].isSelected() == true & e.getSource() == list[sourceIndex].getFileNameLabel()){
                    String s = JOptionPane.showInputDialog(
                            null,
                            "Input New Name",
                            "Rename",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    if(!validString(s)) {
                        while (validString(s) == false){
                            s = JOptionPane.showInputDialog(
                                    null,
                                    "Invalid Character Detected. (/, \\, ?, \", *, <, >, |)",
                                    "Rename",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }

                    if (s != null){
                        list[sourceIndex].setText(s);
                    }
                }


                /* TODO If this was a shift click on a FilePanel, select all in
                the range between previous selection and current selection
                indices */
                
                /* TODO If this was a control click on a FilePanel, toggle its
                selection boolean. */
                /* if (...){
                    bool isSelected = list[sourceIndex].isSelected();
                    list[sourceIndex].select(!isSelected);
                }
                */
                
                /* If this was not a shift click or control click, deselect all
                and select the source of the click. */
                else {
                    for (int i = 0;i < list.length;i++){
                        if (i != sourceIndex){
                            list[i].select(false);
                        }
                        else{
                            list[i].select(true);
                        }
                    }
                }
            }
            
            // Single right click
            else if (CLICKS == 1 && BUTTON == java.awt.event.MouseEvent.BUTTON3){
                /* If the right click was on the DirectoryPanel, deselect all 
                FilePanels and draw a popup menu for sorting. */
                if (sourceIndex == -1){
                    for (int i = 0;i < list.length;i++){
                        list[i].select(false);
                    }
                    // TODO draw a popup menu
                }
                else{
                    /* If the right click was on an unselected FilePanel,
                    deselect all other FilePanels and select it. */
                    if (!list[sourceIndex].isSelected()){
                        for (int i = 0;i < list.length;i++){
                            if (i != sourceIndex){
                                list[i].select(false);
                            }
                            else{
                                list[i].select(true);
                            }
                        }
                    }
                    /* If the right click was on a selected FilePanel, don't
                    select/deselect anything. Nothing else needs to be done. */
                    // TODO draw a popup menu
                    // TODO apply selected operation to all selected FilePanels
                }
            }
        }
    }
    
    /**
     * Overridden NavigatorObserver method.
     * "Refreshes" the directory that is being displayed. This should update any
     * and all FilePanels in the DirectoryPanel.
     * NOTICE: To be called from Navigator.notifyObservers only!
     * @param s the Subject that was updated.
     */
    @Override
    public void update(Subject s) {
        // Check if currentPath == fileNavigator.getDirectory() (if not, do nothing)
        // Remove all filepanels from this
        // Remove all filepanels from list
        // Add new filepanels to list
        // Do something like this:
        /*
        // Get a list of the files from this directory (currentPath)
        
        int i;
        for (i = 0;i < files.length;i++){
            list[i] = new FilePanel(files[i],this);
            layout.putConstraint(SpringLayout.WEST, list[i], 5, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.NORTH, list[i], i*VERTICAL_FP_GAP, SpringLayout.NORTH, this);
            this.add(list[i]);
        }
        */
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

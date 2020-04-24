import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A modified JPanel that will act as our horizontal toolbar.
 * @author Bradley Nickle
 */
public class FileManagerToolbar extends JPanel implements MouseListener,KeyListener,NavigatorObserver{
   /* GUI components; back/forward buttons, address/search bars. */
   private ButtonGroup navButtons;
   private JButton backButton,forwardButton;
   private JTextField addressBar,searchBar;
   /* The Navigator */
   private Navigator nav;
   private String current;

   /**
    * Default FileManagerToolbar constructor.
    */
   public FileManagerToolbar(Navigator n){
       // Observe the Navigator
       nav = n;
       nav.attach(this);
       // Get the current directory
       current = nav.getDirectory();
       
       // Configure layout manager
       setLayout(new FlowLayout(FlowLayout.LEFT));

       // Instantiate a buttongroup for back/forward buttons
       navButtons = new ButtonGroup();

       // Configure buttons
       backButton = new JButton("<-"); // Instantiate the back button
       backButton.setToolTipText("Back"); // add a tooltip
       // add an action listener to it; it won't trigger any code if we don't do this
       backButton.addMouseListener(this);
       navButtons.add(backButton); // add it to the button group
       add(backButton); // add it to the toolbar

       // Repeat all back button steps for the forward button
       forwardButton = new JButton("->");
       forwardButton.setToolTipText("Forward");
       forwardButton.addMouseListener(this);
       navButtons.add(forwardButton);
       add(forwardButton);

       // Configure address bar
       addressBar = new JTextField(16);
       addressBar.setToolTipText("Enter a path");
       addressBar.setText(current);
       addressBar.addMouseListener(this);
       addressBar.addKeyListener(this);
       add(addressBar);

       // Configure search bar
       searchBar = new JTextField(8);
       searchBar.setText("");
       searchBar.setToolTipText("Enter the name of a file you want to search for");
       searchBar.addMouseListener(this);
       searchBar.addKeyListener(this);
       add(searchBar); 
       
       // Adjust button heights to match the address bar!
       int height = searchBar.getPreferredSize().height;
       int width = backButton.getPreferredSize().width;
       backButton.setPreferredSize(new Dimension(width,height));
       width = forwardButton.getPreferredSize().width;
       forwardButton.setPreferredSize(new Dimension(width,height));
       
       // Set a border
        setBorder(BorderFactory.createLineBorder(new Color(128,128,128)));
   }

    /**
     * Overridden NavigatorObserver method.
     * Updates the FileManagerToolbar based on changes in the Navigator, mostly
     * the changes to the Navigator's current directory.
     * @param s the Subject to be updated
     */
    @Override
    public void update(Subject s) {
        /* this only needs to be updated if current doesn't match nav's current
        directory */
        if (!current.equals(nav.getDirectory())){
            current = nav.getDirectory();
            addressBar.setText(current);
        }
    }

    /**
     * Overridden MouseListener method.
     * Used to process clicks for backButton, forwardButton, and more.
     * @param e the event to be processed
     * @author Bradley Nickle
     */
    @Override
    public void mouseClicked(MouseEvent e) {
       if (e.getSource() == backButton){
           // Tell the Navigator to go back
           if (nav.size() > 1 && !nav.isAtBeginning()){
               nav.back();
           }
       }
       else if (e.getSource() == forwardButton){
           /* Tell the Navigator to go forward (to a directory that is in the
           history) */
           if (nav.size() > 1 && !nav.isAtEnd()){
               nav.forward();
           }
       }
       // TODO add in support for sorting.
    }
    
    /**
     * Overridden KeyListener method.
     * @param e the KeyEvent to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Check that the "Enter" or "Return" key was actually pressed.
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            
            // If the source was the address bar, try to change the directory.
            if (e.getSource() == addressBar){
                String potentialDirectory = addressBar.getText();
                
                /* Check if potentialDirectory is a directory before moving to it.
                Then, check if it is visitable. */
                if (nav.isDirectory(potentialDirectory)){
                    if (nav.canVisit(potentialDirectory)){
                        nav.forward(potentialDirectory);
                    }
                }
            }
            
            // If the source was the search bar, try to execute a search.
            else if (e.getSource() == searchBar){
                String search = searchBar.getText();
                
                /* Check that the search bar's text contains an actual search key
                before searching. If it was, set it as the search key. */
                if (search != null){
                    nav.setSearchKey(search);
                }
            }
        }
    }

    /**
     * Overridden MouseListener method.
     * Unused but must be overridden anyway.
     * @param e the MouseEvent to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /**
     * Overridden MouseListener method.
     * Unused but must be overridden anyway.
     * @param e the MouseEvent to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Overridden MouseListener method.
     * Unused but must be overridden anyway.
     * @param e the MouseEvent to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Overridden MouseListener method.
     * Unused but must be overridden anyway.
     * @param e the MouseEvent to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Overridden KeyListener method.
     * Unused but must be overridden anyway.
     * @param e the KeyEvent to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Overridden KeyListener method.
     * Unused but must be overridden anyway.
     * @param e the KeyEvent to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
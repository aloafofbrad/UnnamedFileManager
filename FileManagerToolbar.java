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
       addressBar.setToolTipText("Enter a file path");
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

    @Override
    public void mouseClicked(MouseEvent e) {
        /* In general, this is what actionPerformed() should look like:
       if (e.getSource() == myComponent_1){...;}
       if (e.getSource() == myComponent_2){...;}
       if (e.getSource() == myComponent_3 && someCondition){...;}
       ...
       if (e.getSource() == myComponent_n){...;} */
       if (e.getSource() == backButton){
           // Tell the Navigator to go back
           if (nav.size() > 1 && !nav.isAtBeginning()){
               nav.back();
           }
       }
       if (e.getSource() == forwardButton){
           /* Tell the Navigator to go forward (to a directory that is in the
           history) */
           if (nav.size() > 1 && !nav.isAtEnd()){
               nav.forward();
           }
       }
       if (e.getSource() == addressBar){
       }
       if (e.getSource() == searchBar){
       }
    }
    
    /**
     * Overridden KeyListener method.
     * @param e the KeyEvent to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
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
            if (e.getSource() == searchBar){
                // TODO search for a file
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
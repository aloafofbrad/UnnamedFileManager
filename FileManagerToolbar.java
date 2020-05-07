import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A modified JPanel that will act as our horizontal toolbar.
 * @author Bradley Nickle
 */
public class FileManagerToolbar extends JPanel implements MouseListener,KeyListener,ManagerObserver{
   /* GUI components; back/forward buttons, address/search bars. */
   private ButtonGroup navButtons;
   private JButton backButton,forwardButton;
   private JTextField addressBar,searchBar;
   private ButtonGroup sortButtons;
   private JButton name,type,size,dateModified,dateCreated;
   /* The Manager */
   private Manager mngr;
   private String current;
   private SpringLayout layout;
   private final int HGAP = 5;
   private final int VGAP = 5;

   /**
    * Default FileManagerToolbar constructor.
    */
   public FileManagerToolbar(Manager m){
       // Observe the Manager
       mngr = m;
       mngr.attach(this);
       // Get the current directory
       current = mngr.getDirectory();
       
       // Configure layout manager
       layout = new SpringLayout();
       setLayout(layout);
       int hsum_top = 5;
       int hsum_bottom = 5;
       int vsum = 5;

       // Instantiate a buttongroup for back/forward buttons
       navButtons = new ButtonGroup();

       // Configure buttons
       backButton = new JButton("<-"); // Instantiate the back button
       backButton.setToolTipText("Back"); // add a tooltip
       // add an action listener to it; it won't trigger any code if we don't do this
       backButton.addMouseListener(this);
       layout.putConstraint(SpringLayout.WEST,backButton,hsum_top,SpringLayout.WEST,this);
       layout.putConstraint(SpringLayout.NORTH,backButton,vsum,SpringLayout.NORTH,this);
       navButtons.add(backButton); // add it to the button group
       add(backButton); // add it to the toolbar
       hsum_top += HGAP + backButton.getPreferredSize().width;

       // Repeat all back button steps for the forward button
       forwardButton = new JButton("->");
       forwardButton.setToolTipText("Forward");
       forwardButton.addMouseListener(this);
       layout.putConstraint(SpringLayout.WEST,forwardButton,hsum_top,SpringLayout.WEST,this);
       layout.putConstraint(SpringLayout.NORTH,forwardButton,vsum,SpringLayout.NORTH,this);
       navButtons.add(forwardButton);
       add(forwardButton);
       hsum_top += HGAP + forwardButton.getPreferredSize().width;

       // Configure address bar
       addressBar = new JTextField(32);
       addressBar.setToolTipText("Enter a path");
       addressBar.setText(current);
       addressBar.addMouseListener(this);
       addressBar.addKeyListener(this);
       layout.putConstraint(SpringLayout.WEST,addressBar,hsum_top,SpringLayout.WEST,this);
       layout.putConstraint(SpringLayout.NORTH,addressBar,vsum,SpringLayout.NORTH,this);
       add(addressBar);
       hsum_top += HGAP + addressBar.getPreferredSize().width;

       // Configure search bar
       searchBar = new JTextField(16);
       searchBar.setText("");
       searchBar.setToolTipText("Enter the name of a file you want to search for");
       searchBar.addMouseListener(this);
       searchBar.addKeyListener(this);
       layout.putConstraint(SpringLayout.WEST,searchBar,hsum_top,SpringLayout.WEST,this);
       layout.putConstraint(SpringLayout.NORTH,searchBar,vsum,SpringLayout.NORTH,this);
       add(searchBar);
       hsum_top += HGAP + searchBar.getPreferredSize().width + HGAP;
       vsum += VGAP + searchBar.getPreferredSize().height;
       
       // Instantiate button group for sorting buttons
       sortButtons = new ButtonGroup();
       
       // Configure the sorting buttons
       name = new JButton("Name");
       name.setToolTipText("Sort by file name");
       name.addMouseListener(this);
       sortButtons.add(name);
       layout.putConstraint(SpringLayout.WEST,name,hsum_bottom,SpringLayout.WEST,this);
       layout.putConstraint(SpringLayout.NORTH,name,vsum,SpringLayout.NORTH,this);
       add(name);
       hsum_bottom += HGAP + name.getPreferredSize().width;
       
       type = new JButton("Type");
       type.setToolTipText("Sort by file type");
       type.addMouseListener(this);
       sortButtons.add(type);
       layout.putConstraint(SpringLayout.WEST,type,hsum_bottom,SpringLayout.WEST,this);
       layout.putConstraint(SpringLayout.NORTH,type,vsum,SpringLayout.NORTH,this);
       add(type);
       hsum_bottom += HGAP + type.getPreferredSize().width;
       
       size = new JButton("Size");
       size.setToolTipText("Sort by file size");
       size.addMouseListener(this);
       sortButtons.add(size);
       layout.putConstraint(SpringLayout.WEST,size,hsum_bottom,SpringLayout.WEST,this);
       layout.putConstraint(SpringLayout.NORTH,size,vsum,SpringLayout.NORTH,this);
       add(size);
       hsum_bottom += HGAP + size.getPreferredSize().width;
       
       dateModified = new JButton("Date Modified");
       dateModified.setToolTipText("Sort by date modified");
       dateModified.addMouseListener(this);
       sortButtons.add(dateModified);
       layout.putConstraint(SpringLayout.WEST,dateModified,hsum_bottom,SpringLayout.WEST,this);
       layout.putConstraint(SpringLayout.NORTH,dateModified,vsum,SpringLayout.NORTH,this);
       add(dateModified);
       hsum_bottom += HGAP + dateModified.getPreferredSize().width;
       
       dateCreated = new JButton("Date Created");
       dateCreated.setToolTipText("Sort by date created");
       dateCreated.addMouseListener(this);
       sortButtons.add(dateCreated);
       layout.putConstraint(SpringLayout.WEST,dateCreated,hsum_bottom,SpringLayout.WEST,this);
       layout.putConstraint(SpringLayout.NORTH,dateCreated,vsum,SpringLayout.NORTH,this);
       add(dateCreated);
       hsum_bottom += HGAP + dateCreated.getPreferredSize().width + HGAP;
       
       // Adjust button heights to match the address bar! Thinner buttons look nicer.
       int height = addressBar.getPreferredSize().height;
       int width = backButton.getPreferredSize().width;
       backButton.setPreferredSize(new Dimension(width,height));
       width = forwardButton.getPreferredSize().width;
       forwardButton.setPreferredSize(new Dimension(width,height));
       width = name.getPreferredSize().width;
       name.setPreferredSize(new Dimension(width,height));
       width = type.getPreferredSize().width;
       type.setPreferredSize(new Dimension(width,height));
       width = size.getPreferredSize().width;
       size.setPreferredSize(new Dimension(width,height));
       width = dateModified.getPreferredSize().width;
       dateModified.setPreferredSize(new Dimension(width,height));
       width = dateCreated.getPreferredSize().width;
       dateCreated.setPreferredSize(new Dimension(width,height));
       
       width = this.getPreferredSize().width;
       this.setPreferredSize(new Dimension(width,vsum * 2));
       
       // Set a border
        setBorder(BorderFactory.createLineBorder(new Color(128,128,128)));
   }

    /**
     * Overridden ManagerObserver method.
     * Updates the FileManagerToolbar based on changes in the Manager, mostly
     * the changes to the Manager's current directory.
     * @param s the Subject to be updated
     */
    @Override
    public void update(Subject s) {
        /* this only needs to be updated if current doesn't match mngr's current
        directory */
        if (!current.equals(mngr.getDirectory())){
            current = mngr.getDirectory();
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
           // Tell the Manager to go back
           if (mngr.size() > 1 && !mngr.isAtBeginning()){
               mngr.back();
           }
       }
       else if (e.getSource() == forwardButton){
           /* Tell the Manager to go forward (to a directory that is in the
           history) */
           if (mngr.size() > 1 && !mngr.isAtEnd()){
               mngr.forward();
           }
       }
       else if (e.getSource() == name){
           mngr.setSortAttribute(name.getText());
       }
       else if (e.getSource() == type){
           mngr.setSortAttribute(type.getText());
       }
       else if (e.getSource() == size){
           mngr.setSortAttribute(size.getText());
       }
       else if (e.getSource() == dateModified){
           mngr.setSortAttribute(dateModified.getText());
       }
       else if (e.getSource() == dateCreated){
           mngr.setSortAttribute(dateCreated.getText());
       }
       
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
                if (mngr.isDirectory(potentialDirectory)){
                    if (mngr.canVisit(potentialDirectory)){
                        mngr.forward(potentialDirectory);
                    }
                }
            }
            
            // If the source was the search bar, try to execute a search.
            else if (e.getSource() == searchBar){
                String search = searchBar.getText();
                
                /* Check that the search bar's text contains an actual search key
                before searching. If it was, set it as the search key. */
                if (search != null){
                    mngr.setSearchKey(search);
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
    public void mousePressed(MouseEvent e) {}

    /**
     * Overridden MouseListener method.
     * Unused but must be overridden anyway.
     * @param e the MouseEvent to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {}

    /**
     * Overridden MouseListener method.
     * Unused but must be overridden anyway.
     * @param e the MouseEvent to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {}

    /**
     * Overridden MouseListener method.
     * Unused but must be overridden anyway.
     * @param e the MouseEvent to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * Overridden KeyListener method.
     * Unused but must be overridden anyway.
     * @param e the KeyEvent to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Overridden KeyListener method.
     * Unused but must be overridden anyway.
     * @param e the KeyEvent to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {}
    
    /**
     * Overridden ManagerObserver method. This should not be called,
     * and nothing should happen if it is called.
     * @param s the Subject calling the function
     */
    @Override
    public void search(Subject s) {}

    /**
     * Overridden ManagerObserver method. This should not be called,
     * and nothing should happen if it is called.
     * @param s the Subject calling the function
     */
    @Override
    public void sort(Subject s) {}
}
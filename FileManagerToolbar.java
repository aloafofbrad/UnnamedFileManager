import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A modified JPanel that will act as our horizontal toolbar.
 * @author Bradley Nickle
 */
public class FileManagerToolbar extends JPanel implements ActionListener,NavigatorObserver{
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
       backButton.addActionListener(this);
       navButtons.add(backButton); // add it to the button group
       add(backButton); // add it to the toolbar

       // Repeat all back button steps for the forward button
       forwardButton = new JButton("->");
       forwardButton.setToolTipText("Forward");
       forwardButton.addActionListener(this);
       navButtons.add(forwardButton);
       add(forwardButton);

       // Configure address bar
       addressBar = new JTextField(16);
       addressBar.setToolTipText("Enter a file path");
       addressBar.setText("C:\\");
       addressBar.addActionListener(this);
       add(addressBar);

       // Configure search bar
       searchBar = new JTextField(8);
       searchBar.setText("");
       searchBar.setToolTipText("Enter the name of a file you want to search for");
       searchBar.addActionListener(this);
       add(searchBar); 
   }

   /**
    * Override actionPerformed to utilize the ActionListener code we implemented.
    * @param e the ActionEvent to be processed.
    */
   @Override
   public void actionPerformed(ActionEvent e){
       /* In general, this is what actionPerformed() should look like:
       if (e.getSource() == myComponent_1){...;}
       if (e.getSource() == myComponent_2){...;}
       if (e.getSource() == myComponent_3 && someCondition){...;}
       ...
       if (e.getSource() == myComponent_n){...;} */

       if (e.getSource() == backButton){
           // TODO tell the Navigator to go back
           System.out.println("Back not yet supported");
           System.out.println(addressBar.getText());
       }
       if (e.getSource() == forwardButton){
           // TODO tell the Navigator to go forward
           System.out.println("Forward not yet supported");
       }
       if (e.getSource() == addressBar){
           // TODO determine wether or not enter was pressed, then 
       }
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
            // TODO
        }
    }
}
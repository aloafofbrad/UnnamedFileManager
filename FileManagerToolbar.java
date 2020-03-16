import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A modified JPanel that will act as our horizontal toolbar.
 * @author Bradley Nickle
 */
public class FileManagerToolbar extends JPanel implements ActionListener{
   private ButtonGroup navigation;
   private JButton backButton,forwardButton;
   private JTextField addressBar,searchBar;

   /**
    * Default FileManagerToolbar constructor.
    */
   public FileManagerToolbar(){
       // Configure layout manager
       setLayout(new FlowLayout(FlowLayout.LEFT));

       // Instantiate a buttongroup for back/forward buttons
       navigation = new ButtonGroup();

       // Configure buttons
       backButton = new JButton("<-"); // Instantiate the back button
       backButton.setToolTipText("Back"); // add a tooltip
       // add an action listener to it; it won't trigger any code if we don't do this
       backButton.addActionListener(this);
       navigation.add(backButton); // add it to the button group
       add(backButton); // add it to the toolbar

       // Repeat all back button steps for the forward button
       forwardButton = new JButton("->");
       forwardButton.setToolTipText("Forward");
       forwardButton.addActionListener(this);
       navigation.add(forwardButton);
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
           System.out.println("Back not yet supported");
       }
       if (e.getSource() == forwardButton){
           System.out.println("Forward not yet supported");
       }
   }
}
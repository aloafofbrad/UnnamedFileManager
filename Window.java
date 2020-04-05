/**
 * A modified JFrame class that will serve as the main window of the file manager.
 * @author Bradley Nickle
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Our Window class.
 */
public class Window extends JFrame{
    // Version number
    final private double version = 0.1;
    /***
     * Start a window.
     * @param args Required Java syntax for main()
     */
    public static void main(String[] args){
        Window w = new Window();
    }

    /**
     * Default Window constructor.
     */
    public Window(){
        // Size the Window
        setBounds(100,100,640,480);
        
        // Place the Window on the screen
        setLocationRelativeTo(null);
        
        // Name the Window
        setTitle("File Manager " + version);
        
        // Set up test data NOTE this will eventually be replaced with actual file data
        String path = "C:/";
        
        // Initialize the navigator
        Navigator nav = new Navigator(path);
        
        // Initialize the GUI components
        DirectoryPanel centerPanel = new DirectoryPanel(nav);
        FileManagerToolbar upperToolbar = new FileManagerToolbar(nav);
        FileManagerUI ui = new FileManagerUI(centerPanel,upperToolbar);
        
        // Set up the GUI
        setContentPane(ui);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}

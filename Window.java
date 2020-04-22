/**
 * A modified JFrame class that will serve as the main window of the file manager.
 * @author Ian Ho-Sing-Loy
 * @author Brandon Moore
 * @author Bradley Nickle
 * @author Dan Tran
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Our Window class.
 */
public class Window extends JFrame{
    // Version number
    final private double version = 0.1;
    private DirectoryPanel centerPanel;
    private FileManagerToolbar upperToolbar;
    private ColumnNames columnNames;
    private FileManagerUI ui;
    /***
     * Start a window.
     * @param args Required Java syntax for main()
     */
    public static void main(String[] args){
        Window w = new Window();
    }

    /**
     * Default Window constructor.
     * @author Bradley Nickle
     * @author Ian Ho-Sing-Loy
     */
    public Window(){
        // Size the Window
        setBounds(100,100,640,480);
        
        // Place the Window on the screen
        setLocationRelativeTo(null);
        
        // Name the Window
        setTitle("File Manager " + version);
        
        // Set up test data NOTE this will eventually be replaced with actual file data
        String path = "C:/Users/aloaf/Documents/";
        
        // Initialize the navigator
        Navigator nav = new Navigator(path);
        
        // Initialize the GUI components
        DirectoryPanel centerPanel = new DirectoryPanel(nav);
        FileManagerToolbar upperToolbar = new FileManagerToolbar(nav);
        ColumnNames columnNames = new ColumnNames();
        FileManagerUI ui = new FileManagerUI(centerPanel,upperToolbar, columnNames);
        
        // Set up the GUI
        setContentPane(ui);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        configureCloseOperations();
    }

    /**
     * Configures what to do when the user closes the window.
     * In short, this adds a WindowAdapter that will save the window's size and
     * position on close.
     * @author Dan Tran
     * @author Bradley Nickle
     */
    private void configureCloseOperations(){
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                Window theWindow = (Window) e.getWindow();
                
                File file;
                try {
                    file = new File("sizePosition.txt");
                    if (!file.exists()) {
                        file.createNewFile();
}
                    
                    /* This line is for testing. It should tell you where the program
                    is saving sizePosition.txt to. */
                    System.out.println("path: " + file.getAbsolutePath());

                    // Write size and position data to the file.
                    PrintWriter pw = new PrintWriter(file);
                    pw.println("x = " + theWindow.getX());
                    pw.println("y = " + theWindow.getY());
                    pw.println("h = " + theWindow.getHeight());
                    pw.println("w = " + theWindow.getWidth());
                    pw.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                
                // Close the program.
                theWindow.dispose();
                System.exit(0);
            }
        });
    }
}

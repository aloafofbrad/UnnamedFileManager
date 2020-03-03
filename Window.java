/**
 *
 * @author Bradley Nickle
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Our Window class.
 *
 */
public class Window extends JFrame{
    public static void main(String[] args){
        Window w = new Window();
    }

    /**
     * Default Window constructor.
     */
    public Window(){
        setBounds(100,100,640,480);
        setLocationRelativeTo(null);
        setTitle("Hello world!");
        
        String[] data = {"test zero",
                         "test one",
                         "test two",
                         "test three",
                         "test four",
                         "test five",
                         "test six",
                         "test seven",
                         "test eight",
                         "test nine"};
        DirectoryPanel dp = new DirectoryPanel(data);
        setContentPane(new FileManagerUI(dp,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}

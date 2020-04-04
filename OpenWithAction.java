import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Imports Brandon had
import java.io.File;
import java.io.IOException;

/*import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;*/

/**
 * Action to let the user choose which program to open a file with.
 * @author Brandon Moore
 * @author Brad Nickle
 */
public class OpenWithAction extends AbstractAction {
    private String actionName = "Open with...";
    
    /**
     * Default OpenWithAction constructor.
     * @param key a key to find value with
     * @param value the FilePanel to act on
     */
    public OpenWithAction(String key,Object value){
        super("Open with...",null);
        putValue(key,value);
    }
    
    /**
     * Overridden AbstractAction method.
     * Triggers a specific function based on an event.
     * @param e the triggering event.
     */
    public void actionPerformed(ActionEvent e){
        Component parent = (Component) e.getSource();
        String result = null;
        try{
            result = openFile(parent);
        } catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }
        putValue("result",result);
    }
    
    /**
     * Opens a file
     * @author Brandon Moore
     */
    private String openFile(Component parent) throws IOException {
	JFileChooser fc = new JFileChooser();
	Desktop desktop = Desktop.getDesktop();
        fc.setDialogTitle("Please select a program to open your file");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            //JOptionPane.showMessageDialog(null, fc.getSelectedFile().getAbsolutePath());
            String selected = new String(fc.getSelectedFile().getAbsolutePath());
            File selectedFile = fc.getSelectedFile();
            desktop.open(selectedFile);
            return selected;
        }
	    return null;
    }
}

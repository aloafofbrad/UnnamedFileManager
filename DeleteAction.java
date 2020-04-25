import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Imports Brandon had
import java.io.File;
import java.io.IOException;

/*
// Netbeans can't find javax.jnlp ?
import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;*/

/**
 * Action to open a file with a default program.
 * @author Brandon Moore
 * @author Brad Nickle
 */
public class DeleteAction extends AbstractAction {
    private String actionName = "Delete";
    
    /**
     * Default DeleteAction constructor.
     * @param key a key to find value with
     * @param value the FilePanel to act on
     */
    public DeleteAction(String key,Object value){
        super("Delete",null);
        putValue(key,value);
    }
    
    /**
     * Overridden AbstractAction method.
     * Triggers a specific function based on an event.
     * @param e the triggering event.
     */
    public void actionPerformed(ActionEvent e){
        String result = null;
        try{
            result = deleteFile();
        } catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }
        System.out.println(result);
    }
    
    /**
     * Opens a file with a default associated program.
     * @author Brandon Moore
     * @author Bradley Nickle
     * @return the status of the operation
     */
    private String deleteFile() throws IOException {
        try{
            FilePanel fp = (FilePanel) getValue("FilePanel");
            File target = new File(fp.getFullFileName());
            target.delete();
        } catch (Exception e){
            return e.getMessage();
        }
        return "Success";
    }
}
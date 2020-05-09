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
            FilePanel fp = (FilePanel) getValue("FilePanel");
            result = deleteFile(fp.getAbsolutePath(),fp.getFileName());
            System.out.println("Done");
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
    private String deleteFile(String path,String name) throws IOException {
        try{
            File target = new File(path + "/" + name);
            System.out.println(path + "/" + name);
            if (target.isDirectory()){
                String[] paths = target.list();
                for (int i = 0;i < paths.length;i++){
                    try{
                        File current = new File(path + "/" + name + "/" + paths[i]);
                        if (current.isDirectory()){
                            System.out.println(deleteFile(path + "/" + name,paths[i]));
                        }
                        else{
                            current.delete();
                        }
                    }
                    catch (Exception e){
                        return e.getMessage();
                    }
                }
                target.delete();
            }
            else{
                target.delete();
            }
        } catch (Exception e){
            return e.getMessage();
        }
        return "Success";
    }
}
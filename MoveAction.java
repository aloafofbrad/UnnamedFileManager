import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Imports Brandon had
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/*
// Netbeans can't find javax.jnlp ?
import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;*/

/**
 * Action to transfer a file to a new directory
 * @author Brandon Moore
 * @author Brad Nickle
 */
public class MoveAction extends AbstractAction {
    private String actionName = "Move";
    private String currentDir;
    private static String targetFile;
    private static String fileExt;
    
    /**
     * Default MoveAction constructor.
     * @param key a key to find value with
     * @param value the FilePanel to act on
     * @param the current directory of the directory panel
     */
    public MoveAction(String key,Object value, String current){
        super("Move",null);
        putValue(key,value);
        this.currentDir = current;
        this.targetFile = ((FilePanel) value).getFullFileName();
       int index =  targetFile.toString().lastIndexOf('/');
       int index2 = (targetFile.length());
         fileExt = targetFile.substring(index, index2);
    }
    
    public String getCurrent() {
    	return this.currentDir;
    }
    /**
     * Overridden AbstractAction method.
     * Triggers a specific function based on an event.
     * @param e the triggering event.
     */
    public void actionPerformed(ActionEvent e){
        String result = null;
        Component parent = (Component) e.getSource();
            result = moveDir(parent);
    }
    
    /**
     * Move a directory into a new directory
     * @author Dan Tran
     * @author Brandon Moore
     * @param parent 
     */
    public static String moveDir(Component parent)
    {
    	String selected = new String();
    	JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Please select a destination to transfer your file to");
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
            if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                 selected = fc.getSelectedFile().getAbsolutePath();
               //  System.out.println(targetFile);
               // System.out.println(selected);
                 moveFile(targetFile, selected);
            }
            return "success";
    }
    
    /**
     * Move a directory into a new directory
     * @author Dan Tran
     */
    //Currently doesn't work for moving a folder and its content, only creates a new empty folder 
    public static void moveFile(String currentDir, String newDir)
    {
        Path movefrom = FileSystems.getDefault().getPath(targetFile);
        Path target = FileSystems.getDefault().getPath(newDir+fileExt);
        try
        {
            Files.copy(movefrom,target, StandardCopyOption.REPLACE_EXISTING);
            if (target.isAbsolute()) {
            	System.out.println("true");
            	deleteFile(targetFile);
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Deletes a selected file
     * @author Brandon Moore
     * @author Bradley Nickle
     * @return the status of the operation
     */
    private static String deleteFile(String oldFile) throws IOException {
        try{
            File target = new File(oldFile);
            target.delete();
        } catch (Exception e){
            return e.getMessage();
        }
        return "Success";
    }
}


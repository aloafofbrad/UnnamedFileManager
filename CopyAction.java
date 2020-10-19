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

/**
 * Action to create a copy of a file in a new directory
 * @author Brandon Moore
 * @author Brad Nickle
 */
public class CopyAction extends AbstractAction {
    private String actionName = "Copy";
    private String currentDir;
    private String targetFile;
    private static String fileExt;
    
    /**
     * Default CopyWithAction constructor.
     * @param key a key to find value with
     * @param value the FilePanel to act on
     */
    public CopyAction(String key,Object value, String current){
        super("Copy",null);
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
     * @param parent 
     */
    public String moveDir(Component parent)
    {
    	String selected = new String();
    	JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Please select a destination to place your file copy");
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
   
                 selected = fc.getSelectedFile().getAbsolutePath();
                 copyIt(targetFile, selected);
                 return "success";
            }
            return "success";
    }
    
    /**
     * Move a directory into a new directory
     * @author Dan Tran
     * @author Brandon Moore
     */
    public void copyIt(String currentDir, String newDir)
    {
        Path movefrom = FileSystems.getDefault().getPath(targetFile);
        Path target = FileSystems.getDefault().getPath(newDir + fileExt);
        try
        {
            Files.copy(movefrom,target, StandardCopyOption.REPLACE_EXISTING);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
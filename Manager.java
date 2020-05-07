import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * An object that handles a list of visited directories, as well as executing search
 * and sort functions between FileManagerToolbar and DirectoryPanel.
 * Note that it is unique to each instance of the File Manager; it does not save
 * any history once its memory is returned to the system.
 * @author Bradley Nickle
 */
public class Manager extends Subject{
    // Index representing the currently displayed directory.
    private int index;
    // A list of all directories visitable through the use of the foward/back buttons.
    private ArrayList<String> history;
    // The directory the Manager started at. Cannot use "back" when root == current.
    private String root;
    private String searchKey;
    private String sortAttribute;
    
    /**
     * Parameterized constructor for Navigators.
     * Intended to be used with a default starting directory. Will check if a
     * directory exists before visiting it.
     * For example: Manager m = new Manager("C:/Users/Brad/");
     * @param current The directory to attempt to visit.
     */
    public Manager(String current){
        super();
        history = new ArrayList<String>();
        
        // Try to visit current.  If it doesn't work, try some root directories.
        if (canVisit(current)){
            forward(current);
        }
        else if (canVisit("C:/")){
            forward("C:/");
        }
        else if (canVisit("/")){
            forward("/");
        }
        else{
            index = -1;
            root = null;
        }
        searchKey = null;
        sortAttribute = null;
    }
    
    /**
     * Adds a directory to the history.
     * @param next the directory to be added.
     */
    public void add(String next){
        if (history.size() == 0){
            root = next;
        }
        for (int i = 0;i < history.size();i++){
            if (history.get(i).equals(next)){
                history.remove(i);
            }
        }
        history.add(next);
        index = history.size() - 1;
    }
    
    /**
     * Removes the newest directory from the history.
     * Exception safe; checks if history is already at minimum.
     */
    public void remove(){
        if (history.size() > 1){
            // Remove the most recently added directory
            int i = history.size() - 1;
            System.out.println("Removing " + history.get(i));
            history.remove(i);
            
            // Update the index, if it is now beyond the end of the history
            if (index >= history.size()){
                index = history.size() - 1;
            }
        }
    }
    
    /**
     * Move forward to a directory that is in history.
     * Called when FileManagerToolbar.forwardButton is clicked
     */
    public void forward(){
        int last = history.size() - 1;
        if (index < last){
            index++;
        }
        
        // Set searchKey to null so no searches are executed in the next move.
        setSearchKey(null);
        
        /* Notify DirectoryPanel and FileManagerToolbar that the state may have
        changed. */
        notifyObservers();
    }
    
    /**
     * Move forward to a directory that is not in history.
     * Called when:
     *    -the user double clicks a FilePanel with FilePanel.isDirectory is true
     *    -the user enters a new, valid directory in FileManagerToolbar.addressBar
     *     and presses the "Enter" key
     * @param next the directory to move to
     */
    public void forward(String next){
        int last = history.size() - 1;
        if (index != last){
            int smallestIndex = 0 + index;

            /* Remove any directories "in front of" the current directory.
            a <-> b <-> b1 <-> b2       N
                  ^current              ^next, to be added

            a <-> b <-> b1              N
                  ^current              ^next, to be added

            a <-> b                     N
                  ^current              ^next, to be added
            */
            while (index < last && history.size() > smallestIndex){
                remove();
                last = history.size() - 1;
            }
        }
        
        /* Add the next directory
        a <-> b <-> N
       current^     ^next
        */
        add(next);
        
        // Set searchKey to null so no searches are executed in the next move.
        setSearchKey(null);
        
        /* Notify DirectoryPanel and FileManagerToolbar that the state may have
        changed. */
        notifyObservers();
    }
    
    /**
     * Move back to a directory.
     * Called when FileManagerToolbar.backButton is clicked.
     */
    public void back(){
        int first = 0;
        if (history.size() > 1 && index > first){
            index--;
        }
        
        // Set searchKey to null so no searches are executed in the next move.
        setSearchKey(null);
        
        /* Notify DirectoryPanel and FileManagerToolbar that the state may have
        changed. */
        notifyObservers();
    }

    public void name(){
        executeSort();
    }
    
    /**
     * Checks whether or not a string is an existing directory
     * @param s the string to be validated
     * @return true if s represents an existing directory
     * @author Dan Tran
     */
    public boolean exists(String s){
       File file = new File(s);
       try{
           return file.exists();
       }
       catch(SecurityException se){
           System.out.println(se.getMessage());
           return false;
       }
    }
    
    /**
     * 
     * @param s the file to be tested
     * @return true if the file is a directory
     * @author Bradley Nickle
     */
    public boolean isDirectory(String s){
        if (exists(s)){
            File file = new File(s);
            try{
                return file.isDirectory();
            }
            catch (SecurityException se){
                System.out.println(se.getMessage());
            }
        }
        return false;
    }
    
    /**
     * 
     * @param s the file to be checked
     * @return true if the file can be visited
     */
    public boolean canVisit(String s){
        if (exists(s)){
            File file = new File(s);
            try{
                // If the file doesn't exist, we can't visit it
                if (file.exists()){
                    /* If the file's a directory, we should be able to get the
                    number of files it contains. Otherwise, if permissions are
                    blocking us from doing that, a SecurityException should be
                    thrown. */
                    if (file.isDirectory()){
                        int num = file.list().length;
                        return true;
                    }
                    /* If the file exists and is a File, and no permissions are
                    blocking us, we can visit it. */
                    else if (file.isFile()){
                        return true;
                    }
                }
                else {
                    return false;
                }
            }
            catch(SecurityException se){
                System.out.println(se.getMessage());
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return false;
    }
    
    /**
     * Checks whether or not a string is valid as a file path
     * @param s the string to be validated
     * @return true if s is valid
     * @author Dan Tran
     */
    public boolean validate(String s){
        String arr[] = {"<",">",":","\"","/","\\","|","?","*"};
        for (int i = 0; i < arr.length; i++)
        {
            if(s.contains(arr[i]))
                return false;
        }
         return true;
    }
    
    /**
     * @return true if the current directory is oldest in the history
     */
    public boolean isAtBeginning(){
        int first = 0;
        if (index == first) return true;
        return false;
    }
    
    /**
     * @return true if the current directory is newest in the history
     */
    public boolean isAtEnd(){
        int last = history.size() - 1;
        if (index == last) return true;
        return false;
    }
    
    /**
     * @return true if the current directory is not the oldest or newest in the history
     */
    public boolean isAtMiddle(){
        if (!isAtBeginning() && !isAtEnd()) return true;
        return false;
    }
    
    /**
     * NOT exception safe; WILL throw exceptions. Add at least ONE directory
     * before calling this.
     * @return the address of the current directory
     */
    public String getDirectory(){
        return history.get(index);
    }
    
    /**
     * NOT exception safe; WILL throw exceptions. Add at least ONE directory
     * before calling this.
     * @return the address of a directory in the list
     */
    public String getDirectory(int i){
        return history.get(i);
    }
    
    /**
     * @return the index of the current directory
     */
    public int getIndex(){
        return index;
    }
    
    /**
     * @return the size of the history
     */
    public int size(){
        return history.size();
    }
    
    /**
     * Set a search key to be used in a search.
     * Intended to be called from FileManagerToolbar to DirectoryPanel.
     * @param s the search key
     * @author Bradley Nickle
     */
    public void setSearchKey(String s){
        searchKey = s;
        executeSearch();
    }
    
    /**
     * Get a search key in order to execute a search.
     * Intended to be called in DirectoryPanel.search()
     * @author Bradley Nickle
     * @return the search key
     */
    public String getSearchKey(){
        return searchKey;
    }
    
    /**
     * Set an attribute to sort by.
     * Intended to be called from FileManagerToolbar to DirectoryPanel.
     * @param s the sort attribute
     * @author Bradley Nickle
     */
    public void setSortAttribute(String s){
        sortAttribute = s;
        executeSort();
    }
    
    /**
     * Get a search key in order to execute a search.
     * Intended to be called in DirectoryPanel.sort()
     * @author Bradley Nickle
     * @return the sort attribute
     */
    public String getSortAttribute(){
        return sortAttribute;
    }
}

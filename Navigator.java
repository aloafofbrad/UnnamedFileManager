import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A list of visited directories.
 * Note that it is unique to each instance of the File Manager; it does not save
 * any history once its memory is returned to the system.
 * @author Bradley Nickle
 */
public class Navigator extends Subject{
    private int index;
    private ArrayList<String> history;
    private String root;
    
    /**
     * Default constructor for Navigators.
     * Intended to be used when no default starting directory is available.
     */
    public Navigator(){
        super();
        history = new ArrayList<String>();
        index = -1;
        root = null;
    }
    
    /**
     * Parameterized constructor for Navigators.
     * Intended to be used with a default starting directory.
     * For example: Navigator n = new Navigator("C:/Users/Brad/");
     * @param current 
     */
    public Navigator(String current){
        super();
        history = new ArrayList<String>();
        add(current);
        root = current;
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
        
        /* Notify DirectoryPanel and FileManagerToolbar that the state may have
        changed. */
        notifyObservers();
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
}

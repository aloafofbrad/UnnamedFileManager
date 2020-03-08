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
public class Navigator {
    private int index;
    private ArrayList<String> history;
    
    /**
     * Default constructor for Navigators.
     * Intended to be used when no default starting directory is available.
     */
    public Navigator(){
        history = new ArrayList<String>();
        index = -1;
    }
    
    /**
     * Parameterized constructor for Navigators.
     * Intended to be used with a default starting directory.
     * For example: Navigator n = new Navigator("C:/Users/Brad/");
     * @param current 
     */
    public Navigator(String current){
        history = new ArrayList<String>();
        add(current);
    }
    
    /**
     * Adds a directory to the history.
     * @param next the directory to be added.
     */
    public void add(String next){
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
     * Exception safe; checks if history is already empty.
     */
    public void remove(){
        if (history.size() > 0){
            // Remove the most recently added directory
            int i = history.size() - 1;
            history.remove(i);
            
            // Update the index, if it is now beyond the end of the history
            if (index >= history.size()){
                index = history.size() - 1;
            }
        }
    }
    
    /**
     * Move forward to a directory that is in history.
     */
    public void forward(){
        int last = history.size() - 1;
        if (index < last){
            index++;
        }
    }
    
    /**
     * Move forward to a directory that is not in history.
     * @param next 
     */
    public void forward(String next){
        int last = history.size() - 1;
        
        /* Remove any directories "in front of" the current directory.
        a <-> b <-> b1 <-> b2       N
              ^current              ^next, to be added
        
        a <-> b <-> b1              N
              ^current              ^next, to be added
        
        a <-> b                     N
              ^current              ^next, to be added
        */
        while (index != last){
            remove();
        }
        
        /* Add the next directory
        a <-> b <-> N
       current^     ^next
        */
        add(next);
        
        /* Update the current directory essentially, (current = next)
        a <-> b <-> N
                    ^current
        */
        index = history.size() - 1;
    }
    
    /**
     * Move back to a directory.
     */
    public void back(){
        int first = 0;
        if (history.size() > 1 && index > first){
            index--;
        }
    }
    
    /**
     * @return true if the current directory is oldest in the history
     */
    public boolean isBeginning(){
        int first = 0;
        if (index == first) return true;
        return false;
    }
    
    /**
     * @return true if the current directory is newest in the history
     */
    public boolean isEnd(){
        int last = history.size() - 1;
        if (index == last) return true;
        return false;
    }
    
    /**
     * @return true if the current directory is not the oldest or newest in the history
     */
    public boolean isMiddle(){
        if (!isBeginning()){ if (!isEnd()){ return true;}}
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

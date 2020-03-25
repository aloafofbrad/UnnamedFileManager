import java.util.ArrayList;

/**
 *
 * @author Bradley Nickle
 */
public abstract class Subject {
    /* The observers to be observed. Currently intended to be DirectoryPanel and
    FileManagerToolbar. */
    private ArrayList<NavigatorObserver> observers;
    
    /**
     * Default constructor for Subjects.
     */
    public Subject(){
        observers = new ArrayList<NavigatorObserver>();
    }
    
    /**
     * @param n the NavigatorObserver to be attached.
     */
    public void attach(NavigatorObserver n){
        observers.add(n);
    }
    
    /**
     * @param n the NavigatorObserver to be detached.
     */
    public void detach(NavigatorObserver n){
        observers.remove(n);
    }
    
    /**
     * Notify the DirectoryPanel and FileManagerToolbar that the directory has
     * changed.
     */
    public void notifyObservers(){
        for (int i = 0;i < observers.size();i++){
            observers.get(i).update(this);
        }
    }
}

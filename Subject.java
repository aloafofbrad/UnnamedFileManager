import java.util.ArrayList;

/**
 *
 * @author Bradley Nickle
 */
public abstract class Subject {
    /* The observers to be observed. Intended to be DirectoryPanel,
    FileManagerToolbar, and FileManagerUI. */
    private ArrayList<ManagerObserver> observers;
    private int mainPanelIndex;
    
    /**
     * Default constructor for Subjects.
     */
    public Subject(){
        super();
        observers = new ArrayList<ManagerObserver>();
        mainPanelIndex = -1;
    }
    
    /**
     * @param m the ManagerObserver to be attached.
     */
    public void attach(ManagerObserver m){
        observers.add(m);
        if (m instanceof DirectoryPanel){
            mainPanelIndex = observers.size() - 1;
        }
    }
    
    /**
     * @param m the ManagerObserver to be detached.
     */
    public void detach(ManagerObserver m){
        observers.remove(m);
        if (m instanceof DirectoryPanel){
            mainPanelIndex = -1;
        }
    }
    
    /**
     * Notify the DirectoryPanel, and FileManagerToolbar that the directory has
     * changed.
     */
    public void notifyObservers(){
        for (int i = 0;i < observers.size();i++){
            observers.get(i).update(this);
        }
    }
    
    /**
     * Tell the main DirectoryPanel to execute a search on its list of FilePanels.
     * It will use data from the calling subclass instance to decide which
     * attribute to sort by (see Manager.searchKey).
     */
    public void executeSearch(){
        if (mainPanelIndex != -1){
            observers.get(mainPanelIndex).search(this);
        }
        else{
            for (int i = 0;i < observers.size();i++){
                if (observers.get(i) instanceof DirectoryPanel){
                    observers.get(i).search(this);
                    mainPanelIndex = i;
                }
            }
        }
    }
    
    /**
     * Tell DirectoryPanel to sort its list of FilePanels. DirectoryPanel will
     * use data from the calling subclass instance to decide which attribute to
     * sort by (see Manager.sortAttribute).
     */
    public void executeSort(){
        if (mainPanelIndex != -1){
            observers.get(mainPanelIndex).sort(this);
        }
        else{
            for (int i = 0;i < observers.size();i++){
                if (observers.get(i) instanceof DirectoryPanel){
                    observers.get(i).sort(this);
                    mainPanelIndex = i;
                }
            }
        }
    }
}

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

/**
 * Modified JPanel that will display the contents of a directory (its files and
 * subdirectories). In addition, it also allows the user to interact with the
 * files, which are graphically represented as FilePanels.
 * @author Bradley Nickle
 */
public class DirectoryPanel extends JPanel implements MouseListener,ManagerObserver {
    // File path of the current directory; this is the one that will be displayed
    private String currentPath;
    // Contents of the current directory
    private FilePanel[] list;
    /* SpringLayout is the one we want for this one. Define the positions of
    components with respect to the positions of other components. This is important
    for the vertical axis, but not so much for the horizontal axis. GridBagLayout
    and GridLayout both failed to fit our desire to emulate Windows File Explorer's
    display of folder contents. */
    private SpringLayout layout;
    // Visual size of this. NOTE might remove; not sure.
    private Dimension size;
    /* Vertical gap that exists between the top of one FilePanel and the top of
    another, measured in pixels. There's no horizontal equivalent to this since
    we're only currently displaying files in a vertical list view. */
    private int VERTICAL_FP_GAP = 25;
    private final int HORIZONTAL_FP_GAP = 0;
    /**/
    private Manager mngr;
    /**/
    private Timer t;
    /**/
    private boolean wasDoubleClick;
    /* Represents the attribute the FilePanels are currently sorted by. The 
    default value is "Name". Accepted values are gotten directly from
    FileManagerToolbar sort buttons, using their getText() methods. See
    FileManagerToolbar.mouseClicked() for an example. */
    private String currentSort;
    
    /**
     * Default constructor for DirectoryPanels.
     * @param n sets this.currentDirectory
     * @author Bradley Nickle
     * @author Dan Tran
     */
    public DirectoryPanel(Manager n){
        // Configure & observe mngr
        mngr = n;
        mngr.attach(this);
        
        // Get a list of the current directory's contents
        currentPath = mngr.getDirectory();
        File currentDirectory = new File(currentPath);
        String[] files = currentDirectory.list();
        
        // The files come out of File.list() sorted by name.
        currentSort = "Name";
        
        /* Set up the DirectoryPanel's GUI components. If the directory is empty
        or nonexistent, none will be generated, leaving the DirectoryPanel blank. */
        layout = new SpringLayout();
        setLayout(layout);
        try{
            list = new FilePanel[files.length];
            for (int i = 0; i < files.length;i++){
                list[i] = new FilePanel(files[i],this);
                if (i == 0){
                    VERTICAL_FP_GAP = list[i].getPreferredSize().height;
                }
                layout.putConstraint(SpringLayout.WEST, list[i], HORIZONTAL_FP_GAP, SpringLayout.WEST, this);
                layout.putConstraint(SpringLayout.NORTH, list[i], i*VERTICAL_FP_GAP, SpringLayout.NORTH, this);
                this.add(list[i]);
            }
            size = new Dimension(300,files.length*VERTICAL_FP_GAP);
        }
        catch(NullPointerException npe){
            System.out.println(npe.getMessage());
            list = new FilePanel[0];
            size = new Dimension(300,VERTICAL_FP_GAP);
        }
        
        this.addMouseListener(this);
        /* Configure the DirectoryPanel to be scrollable by setting the size and
        calling setAutoscrolls(). */
        this.setPreferredSize(size);
        this.setAutoscrolls(true);
        
        // Set the background color.
        setBackground(Color.white);
    }
    
    /**
     * @return the directory currently being represented by the DirectoryPanel
     */
    public String getCurrentPath(){
        return currentPath;
    }
    
    /**
     * @return the size of the panel
     */
    @Override
    public Dimension getPreferredSize(){
        return size;
    }
    
    /**
     * Checks each FilePanel in a DirectoryPanel's list to see if it was the 
     * source of an event.
     * @param o the source.
     * @return the index of the source FilePanel, or -1 if the source was the
     *         DirectoryPanel, or -999 if it was neither.
     * @author Bradley Nickle
     */
    public int findSource(Object o){
        final int NOSOURCEFOUND = -999;
        if (this == o){
            return -1;
        }
        for (int i = 0;i < list.length;i++){
            if (list[i].hasSource(o)){
                return i;
            }
        }
        return NOSOURCEFOUND;
    }
    
    /**
     * @param s string to be processed
     * @author Ian Ho-Sing-Loy
     * @author Bradley Nickle
     * @return false if a string contains illegal characters or is null/empty
     * */
    public boolean validString(String s){
        char forbidden[] = {'/', '\\', '?', '*', '"', '<', '>', '|'};
        
        if (s == null) return false;
        if (s.isEmpty()) return false;

        for(int i = 0; i < s.length(); i++){
            for(int j = 0; j < 8; j++){
                if(s.charAt(i) == forbidden[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     * @author Bradley Nickle
     * @author Dan Tran
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        /* First, discern what was clicked on. If nothing was clicked, nothing
        needs to happen. */
        int sourceIndex = findSource(e.getSource());
        if (sourceIndex != -999){
            // Get the number of clicks. Credit to Dan Tran
            final int CLICKS = e.getClickCount();
            final int BUTTON = e.getButton();
            
            // Double click (with left mouse button)
            if (CLICKS == 2 && BUTTON == java.awt.event.MouseEvent.BUTTON1){
                /* If the double click was on the DirectoryPanel, deselect all
                FilePanels. The following condition is equivalent to:
                 "if (the e.getSource() == this)"
                */
                System.out.println("Double Click");
                if (sourceIndex == -1){
                    for (int i = 0;i < list.length;i++){
                        list[i].select(false);
                    }
                }
                
                /* If the double click was on a FilePanel, open the file. */
                else{
                    if (list[sourceIndex].isDirectory())
                    {
                        try{
                            if (mngr.canVisit(list[sourceIndex].getFullFileName())){
                                mngr.forward(list[sourceIndex].getFullFileName());
                                currentPath = mngr.getDirectory();
                            }
                        }
                        catch(NullPointerException npe){
                            System.out.println(npe.getMessage());
                        }
                    }
                    else{
                        /* Open using the same code from the right-click menu, for
                        consistency. */
                        OpenAction open = new OpenAction("FilePanel",list[sourceIndex]);
                        open.actionPerformed(new ActionEvent(e.getSource(),0,""));
                    }
                }

                wasDoubleClick = true;
            } else {
                // This is how fast the double click is
                int clickInterval = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval") / 3;

                /* Create timer to interval of clickInterval to call ActionListener
                to do the single click if wasDoubleClick is false. Basically, if
                the time between clicks is greater than clickInterval, it will
                register a single click rather than a double click. */
                t = new Timer(clickInterval, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e1) {
                        if(wasDoubleClick){
                            wasDoubleClick = false;
                        } else {
                            System.out.println("Single Click");
                            // Single left click
                            if (BUTTON == java.awt.event.MouseEvent.BUTTON1) {
                                /* If this was any kind of click on the DirectoryPanel, deselect
                                all FilePanels. */
                                if (sourceIndex == -1) {
                                    for (int i = 0; i < list.length; i++) {
                                        list[i].select(false);
                                    }
                                }

                                /* If file pane was initially selected and the filename was single
                                clicked on the second time, rename */
                                else if (list[sourceIndex].isSelected() && e.getSource() == list[sourceIndex].getFileNameLabel()) {
                                    String newName = JOptionPane.showInputDialog(
                                            null,
                                            "Input New Name",
                                            "Rename",
                                            JOptionPane.INFORMATION_MESSAGE
                                    );

                                    // If the user entered an invalid filename, let them try again
                                    if (!validString(newName)) {
                                        while (validString(newName) == false && newName != null) {
                                            newName = JOptionPane.showInputDialog(
                                                    null,
                                                    "Invalid Character Detected. (/, \\, ?, \", *, <, >, |)",
                                                    "Rename",
                                                    JOptionPane.ERROR_MESSAGE
                                            );
                                        }
                                    }

                                    /* If the user didn't cancel, rename the file.
                                    newName == null if the user cancelled. */
                                    if (newName != null && !newName.isEmpty()) {
                                        String extension = "";
                                        File source = new File(list[sourceIndex].getFullFileName());

                                        if(!list[sourceIndex].isDirectory()){
                                            extension = list[sourceIndex].getFileName().substring(list[sourceIndex].getFileName().lastIndexOf("."));
                                        }

                                        newName += extension;
                                        source.renameTo(new File(currentPath + '\\' + newName));
                                        list[sourceIndex].setText(newName);
                                    }
                                }

                                /* If this was not a shift click or control click, deselect all
                                and select the source of the click. */
                                else {
                                    for (int i = 0; i < list.length; i++) {
                                        if (i != sourceIndex) {
                                            list[i].select(false);
                                        } else {
                                            list[i].select(true);
                                        }
                                    }
                                }
                            }

                            // Single right click
                            else if (BUTTON == java.awt.event.MouseEvent.BUTTON3) {
                                /* If the right click was on the DirectoryPanel, deselect all
                                FilePanels and draw a popup menu for sorting. */
                                if (sourceIndex == -1) {
                                    for (int i = 0; i < list.length; i++) {
                                        list[i].select(false);
                                    }
                                    // TODO draw a popup menu ?
                                } else {
                                    /* If the right click was on an unselected FilePanel,
                                    deselect all other FilePanels and select it. */
                                    if (!list[sourceIndex].isSelected()) {
                                        for (int i = 0; i < list.length; i++) {
                                            if (i != sourceIndex) {
                                                list[i].select(false);
                                            } else {
                                                list[i].select(true);
                                            }
                                        }
                                    }
                                    /* If the right click was on a selected FilePanel, don't
                                    select/deselect anything. Just configure the right click
                                    menu. */
                                    JPopupMenu rightClickFileMenu = new JPopupMenu("File");

                                    /* Add options to the menu (open, open with, rename, etc...) */
                                    OpenAction open = new OpenAction("FilePanel", list[sourceIndex]);
                                    rightClickFileMenu.add("Open").setAction(open);
                                    MoveAction moveTo = new MoveAction("FilePanel", list[sourceIndex], currentPath);
                                    rightClickFileMenu.add("Move").setAction(moveTo);
                                    CopyAction copyTo = new CopyAction("FilePanel", list[sourceIndex], currentPath);
                                    rightClickFileMenu.add("Copy").setAction(copyTo);
                                    DeleteAction delete = new DeleteAction("FilePanel", list[sourceIndex]);
                                    rightClickFileMenu.add("Delete").setAction(delete);

                                    // TODO update?

                                    JComponent jc = (JComponent) e.getSource();
                                    rightClickFileMenu.show(jc, e.getX(), e.getY());
                                }
                            }
                        }
                    }
                });

                // Ensure that the timer does not repeat
                t.setRepeats(false);
                t.start();
            }
        }
    }
    
    /**
     * Overridden ManagerObserver method.
     * "Refreshes" the directory that is being displayed. This should update any
     * and all FilePanels in the DirectoryPanel.
     * NOTICE: To be called from Manager.notifyObservers only!
     * @param s the Subject that was updated.
     * @author Dan Tran
     * @author Bradley Nickle
     */
    @Override
    public void update(Subject s) {

        // Move to the new directory
        File currentDirectory = new File(mngr.getDirectory());
        currentPath = mngr.getDirectory();
        String[] files = currentDirectory.list();
        
        // Hide & remove old FilePanels
        int i;
        for (i = 0;i < list.length;i++){
            list[i].removeAll();
            list[i].removeMouseListener(this);
            list[i].setVisible(false);
            this.remove(list[i]);
        }
        
        // Set up FilePanels for the new directory
        try{
            list = new FilePanel[files.length];
            for (i = 0;i < files.length;i++){
                list[i] = new FilePanel(files[i],this);
                if (i == 0){
                    VERTICAL_FP_GAP = list[i].getPreferredSize().height;
                }
                layout.putConstraint(SpringLayout.WEST, list[i], HORIZONTAL_FP_GAP, SpringLayout.WEST, this);
                layout.putConstraint(SpringLayout.NORTH, list[i], i*VERTICAL_FP_GAP, SpringLayout.NORTH, this);
                this.add(list[i]);
            }
            System.out.println(i + " FilePanels generated / " + files.length + " files");
            int width = this.getPreferredSize().width;
            size = new Dimension(width,files.length*VERTICAL_FP_GAP);
        }
        /* Caught when unable to list contents/read data of a directory, but the
        file still exists (files.length will be null, causing the exception).
        Notable cases: shortcuts that have listing/reading permissions denied
        system-wide. */
        catch (NullPointerException npe){
            System.out.println("NPE\n" + npe.getMessage());
            list = new FilePanel[1];
            list[0] = new FilePanel(npe.getMessage(),this);
            layout.putConstraint(SpringLayout.WEST,list[0],HORIZONTAL_FP_GAP,SpringLayout.WEST,this);
            layout.putConstraint(SpringLayout.NORTH,list[0],0*VERTICAL_FP_GAP,SpringLayout.NORTH,this);
            this.add(list[0]);
        }
        catch (Exception e){
            System.out.println("Exception\n" + e.getMessage());
            list = new FilePanel[1];
            list[0] = new FilePanel(e.getMessage(),this);
            layout.putConstraint(SpringLayout.WEST,list[0],HORIZONTAL_FP_GAP,SpringLayout.WEST,this);
            layout.putConstraint(SpringLayout.NORTH,list[0],0*VERTICAL_FP_GAP,SpringLayout.NORTH,this);
            this.add(list[0]);
        }
        
        this.setPreferredSize(size);
        revalidate();
        doLayout();
    }
    
    /**
     * Overridden ManagerObserver method.
     * @param s the Manager triggering the search.
     */
    @Override
    public void search(Subject s) {
        // If list isn't empty and searchKey is valid, search.
        String searchKey = mngr.getSearchKey();
        if (list.length > 0 && searchKey != null){
            search(searchKey);
        }
    }
    
    /**
     * Linear search for a specific filename in the current directory.
     * Does not return anything, but calls FilePanel.select(true) on any matches.
     * This highlights any FilePanels whose filenames match.
     * Also calls select(false) on any non-matches, unhighlighting them.
     * 
     * Intended to be called by DirectoryPanel.search(Subject).
     * 
     * @param searchKey the filename to be searched for
     * @author Bradley Nickle
     */
    public void search(String searchKey){
        for (int i = 0;i < list.length;i++){
            if (searchKey.equals(list[i].getFileName())){
                list[i].select(true);
            }
            else{
                list[i].select(false);
            }
        }
    }

    /**
     * Overridden ManagerObserver method.
     * Calls various functions to sort FilePanels in DirectoryPanel.list by 
     * various attributes. Then, rearranges the sorted FilePanels on the screen.
     * No action is necessary if there are fewer than 2 FilePanels, or if the
     * same sort is run twice in a row, since the list will already be in sorted
     * order.
     * @param s the Manager triggering the sort.
     * @author Dan Tran
     * @author Bradley Nickle
     */
    @Override
    public void sort(Subject s) {
        if (list.length > 1){
            String newSort = mngr.getSortAttribute();
            if (!currentSort.equals(newSort)){
                /*
                TODO implement code to select which sort to do
                This function can be used as a wrapper to call the functions that will
                actually execute the sorts. 
                i.e.
                if (name) { this.sortByName(); }
                else if (type) { this.sortByType(); }
                ...

                */
                if (newSort.equals("Type")){
                    sortByType();
                }
                currentSort = newSort;
            }
            
            // After the sorting is done, rearrange the FilePanels on-screen.
            for (int i = 0; i < list.length; i++) {
                System.out.println(i + ". " + list[i].getFileName());

                // Remove old graphical constraints for the FilePanel
                layout.removeLayoutComponent(list[i]);

                // Implement new graphical constraints for the FilePanel
                layout.putConstraint(SpringLayout.WEST, list[i], HORIZONTAL_FP_GAP, SpringLayout.WEST, this);
                layout.putConstraint(SpringLayout.NORTH, list[i], i * VERTICAL_FP_GAP, SpringLayout.NORTH, this);
            }

            // Reset the DirectoryPanel
            revalidate();
            doLayout();
        }
    }
    
    /**
     * Sorts this.list's FilePanels in TODO order by filename.
     * @author Dan Tran
     */
    public void sortByName(){
        System.out.println("Sorting by name...");
        for (int i = 0;i < list.length;i++){
            System.out.println(i + ". " + list[i].getFileName());
        }
        
        // Sort list
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list.length - i - 1; j++) {
                if (list[j].getFileName().toLowerCase().compareTo(list[j + 1].getFileName().toLowerCase()) > 0) {
                    FilePanel temp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = temp;
                }
            }
        }
        System.out.println("Sorted.");
    }
    
    /**
     * Sorts this.list's FilePanels in TODO order by size.
     * @author Dan Tran
     */
    public void sortBySize(){
        System.out.println("Sorting by type...");
        for (int i = 0;i < list.length;i++){
            System.out.println(i + ". " + list[i].getFileName());
        }
        
        // Sort list
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list.length - i - 1; j++) {
                if (list[j].getFileSize().compareTo(list[j + 1].getFileSize()) > 0) {
                    FilePanel temp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = temp;
                }
            }
        }
        System.out.println("Sorted.");
    }
    
    /**
     * Sorts this.list's FilePanels in TODO order by file extension.
     * @author Dan Tran
     */
    public void sortByType(){
        System.out.println("Sorting by type...");
        for (int i = 0;i < list.length;i++){
            System.out.println(i + ". " + list[i].getFileName());
        }
        
        // Sort list
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list.length - i - 1; j++) {
                if (list[j].getFileType().compareTo(list[j + 1].getFileType()) > 0) {
                    FilePanel temp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = temp;
                }
            }
        }
        System.out.println("Sorted.");
    }
    
    /**
     * Sorts this.list's FilePanels in TODO order by date of last modification.
     * @author Dan Tran
     */
    public void sortByDateModified(){
        System.out.println("Sorting by date modified...");
        for (int i = 0;i < list.length;i++){
            System.out.println(i + ". " + list[i].getFileName());
        }
        
        // Sort list
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list.length - i - 1; j++) {
                if (list[j].getDateModified() > list[j + 1].getDateModified()) {
                    FilePanel temp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = temp;
                }
            }
        }
        System.out.println("Sorted.");
    }
    
    /**
     * Sorts this.list's FilePanels in TODO order by date of creation.
     * @author Dan Tran
     */
    public void sortByDateCreated(){
        System.out.println("Sorting by type...");
        for (int i = 0;i < list.length;i++){
            System.out.println(i + ". " + list[i].getFileName());
        }
        
        // Sort list
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list.length - i - 1; j++) {
                if (list[j].getDateCreated() > list[j + 1].getDateCreated()) {
                    FilePanel temp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = temp;
                }
            }
        }
        System.out.println("Sorted.");
    }

    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     * @author Ian Ho-Sing-Loy
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        int sourceIndex = findSource(e.getSource());
        if(sourceIndex >= 0 && list[sourceIndex].isSelected() == false) {
            list[sourceIndex].setBackground(new Color(127,127,127));
        }
    }

    /**
     * Overridden MouseListener method.
     * @param e the MouseEvent to be processed.
     * @author Ian Ho-Sing-Loy
     */
    @Override
    public void mouseExited(MouseEvent e) {
        int sourceIndex = findSource(e.getSource());
        if(sourceIndex >= 0 && list[sourceIndex].isSelected() == false) {
            list[sourceIndex].setBackground(new Color(255, 255, 255));
        }
    }
}
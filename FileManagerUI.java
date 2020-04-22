import java.awt.*;
import javax.swing.*;

/**
 * A slightly modified JScrollPane class that will act as the main part of the UI.
 * @author Bradley Nickle
 */
public class FileManagerUI extends JScrollPane implements NavigatorObserver{
    // Vertical and horizontal scrollbar visibility policies
    private int vsb,hsb;
    private Navigator nav;

    /**
     * Default FileManagerUI constructor.
     * @param view the viewport to be passed into the JScrollPane constructor.
     *     Intended to be a DirectoryPanel (see DirectoryPanel.java)
     * @param colHeader the Component to be viewed as the upper toolbar
     *     Intended to be a FileManagerToolbar
     * @param name
     */
    public FileManagerUI(Navigator n,Component view,Component colHeader,Component name) {
        // Call super()
        super(view,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // NOTE I might remove these data members, but I will keep them in case we need to get the scrollbar policies.
        this.vsb = VERTICAL_SCROLLBAR_AS_NEEDED;
        this.hsb = HORIZONTAL_SCROLLBAR_AS_NEEDED;
        // Enable mouse wheel scrolling
        setWheelScrollingEnabled(true);
        // Set the column header to be a FileManagerToolbar (see FileManagerToolbar.java)
        // We don't really need to store the FileManagerToolbar as a member, since getColumnHeader() isn't being overridden
        setColumnHeaderView(colHeader);

        nav = n;
        nav.attach(this);
        
        // Increase the distance scrolled by per scroll; the default is somewhat slow.
        verticalScrollBar.setUnitIncrement(16);
        horizontalScrollBar.setUnitIncrement(16);
    }
    
    /**
     * Overridden NavigatorObserver method.
     * Sends the scrollbars back to their minimum positions when a directory is
     * loaded or updated. This will usually happen when the user visits a new
     * directory.
     * 
     * The vertical scrollbar will be set as high as possible.
     * The horizontal scrollbar will be set as far left as possible.
     * @param s the subject triggering the update
     */
    @Override
    public void update(Subject s){
        int minimum = verticalScrollBar.getMinimum();
        verticalScrollBar.setValue(minimum);
        minimum = horizontalScrollBar.getMinimum();
        horizontalScrollBar.setValue(minimum);
    }
}
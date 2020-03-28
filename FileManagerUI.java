import java.awt.*;
import javax.swing.*;

/**
 * A slightly modified JScrollPane class that will act as the main part of the UI.
 * @author Bradley Nickle
 */
public class FileManagerUI extends JScrollPane{
    // Vertical and horizontal scrollbar visibility policies
    private int vsb,hsb;

    /**
     * Default FileManagerUI constructor.
     * @param vp the viewport to be passed into the JScrollPane constructor.
     *     Intended to be a DirectoryPanel (see DirectoryPanel.java)
     * @param colHeader the Component to be viewed as the upper toolbar
     *     Intended to be a FileManagerToolbar
     */
    public FileManagerUI(Component view,Component colHeader){
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
    }
}
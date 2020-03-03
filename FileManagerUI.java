import java.awt.*;
import javax.swing.*;

/**
 * A modified JScrollPane class that will act as the main part of the UI.
 * @author Bradley Nickle
 */
public class FileManagerUI extends JScrollPane{
    private int vsb,hsb;

    /**
     * Default FileManagerUI constructor.
     */
    public FileManagerUI(Component vp,int vsb,int hsb){
        super(vp,vsb,hsb);
        this.vsb = vsb;
        this.hsb = hsb;
        setWheelScrollingEnabled(true);
        setColumnHeaderView(new FileManagerToolbar());
        //getViewport().setView(viewport);
        //getViewport().setViewSize(viewport.getSize());
        //setPreferredSize(viewport.getPreferredSize());
        //getViewport().setView(comp);
        //getViewport().setViewSize(comp.getPreferredSize());
    }
}
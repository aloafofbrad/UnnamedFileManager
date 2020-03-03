import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Bradley Nickle
 */
public class FilePanel extends JPanel implements MouseListener{
    private JTextField filename;
    private JLabel size,dateCreated,dateModified;
    
    public FilePanel(String fn){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        filename = new JTextField(fn);
        filename.setEditable(false);
        filename.addMouseListener(this);
        add(filename);
        size = new JLabel("0");
        size.addMouseListener(this);
        add(size);
        dateCreated = new JLabel("1/1/1970 00:00");
        dateCreated.addMouseListener(this);
        add(dateCreated);
        dateModified = new JLabel("1/1/1970 00:00");
        dateModified.addMouseListener(this);
        add(dateModified);
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!filename.isEditable()){
            filename.setEditable(true);
        }
        else{
            filename.setEditable(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
}

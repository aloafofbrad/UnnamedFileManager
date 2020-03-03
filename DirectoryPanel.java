import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Bradley Nickle
 */
public class DirectoryPanel extends JPanel implements MouseListener {
    private FilePanel[] list;
    //private GridBagLayout layout;
    //private GridLayout layout;
    private SpringLayout layout;
    private Dimension size;
    private final int VERTICAL = 32;
    public DirectoryPanel(String[] files){
        //super();
        //list = new JLabel[files.length];
        list = new FilePanel[files.length];
        //layout = new GridBagLayout();
        //layout = new GridLayout(0,1,0,0);
        layout = new SpringLayout();
        setLayout(layout);
        //GridBagConstraints gbc = new GridBagConstraints();
        //gbc.fill = GridBagConstraints.BOTH;
        int i;
        for (i = 0;i < files.length;i++){
            //list[i] = new JLabel(files[i]);
            //list[i].addMouseListener(this);
            list[i] = new FilePanel(files[i]);
            //layout.setConstraints(list[i], gbc);
            layout.putConstraint(SpringLayout.WEST, list[i], 5, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.NORTH, list[i], i*VERTICAL, SpringLayout.NORTH, this);
            this.add(list[i]);
        }
        this.addMouseListener(this);
        size = new Dimension(300,files.length*VERTICAL);
        this.setMinimumSize(size);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        
        /*System.out.println("Hgap: " + layout.getHgap());
        System.out.println("Vgap: " + layout.getVgap());*/
        
        this.setAutoscrolls(true);
    }
    
    @Override
    public Dimension getPreferredSize(){
        return size;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0;i < list.length;i++){
            if (e.getSource() == list[i]){
                System.out.println(list[i].getText());
            }
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

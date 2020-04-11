import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ColumnNames extends JPanel implements MouseListener{
    private ButtonGroup columnButtons;
    private JButton filename, size, dateCreated, dateModified;

    public ColumnNames(){
        columnButtons = new ButtonGroup();

        // Configure buttons
        filename = new JButton("Name"); // Instantiate the back button
        // add an action listener to it; it won't trigger any code if we don't do this
        filename.addMouseListener(this);
        columnButtons.add(filename); // add it to the button group
        add(filename); // add it to the toolbar

        size = new JButton("Size"); // Instantiate the back button
        // add an action listener to it; it won't trigger any code if we don't do this
        size.addMouseListener(this);
        columnButtons.add(size); // add it to the button group
        add(size); // add it to the toolbar

        dateCreated = new JButton("Date Created"); // Instantiate the back button
        // add an action listener to it; it won't trigger any code if we don't do this
        dateCreated.addMouseListener(this);
        columnButtons.add(dateCreated); // add it to the button group
        add(dateCreated); // add it to the toolbar

        dateModified = new JButton("Date Modified"); // Instantiate the back button
        // add an action listener to it; it won't trigger any code if we don't do this
        dateModified.addMouseListener(this);
        columnButtons.add(dateModified); // add it to the button group
        add(dateModified); // add it to the toolbar


    }

    @Override
    public void mouseClicked(MouseEvent e){
        if(e.getSource() == filename){

        } else if (e.getSource() == size){

        } else if (e.getSource() == dateCreated){

        } else if (e.getSource() == dateModified){

        }
    }

    @Override
    public void mousePressed(MouseEvent e){

    }

    @Override
    public void mouseReleased(MouseEvent e){

    }

    @Override
    public void mouseEntered(MouseEvent e){

    }

    @Override
    public void mouseExited(MouseEvent e){

    }
}

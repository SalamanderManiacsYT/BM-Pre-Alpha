package BlockMiner;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Mouse extends MouseAdapter implements MouseMotionListener {
	
	public boolean down = false;

    public int x = 0;

    public int y = 0;

    public Mouse() {}

    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    public void mousePressed(MouseEvent e) {  
        down = true;
    }

    public void mouseClicked(MouseEvent e) {
        //temp stub
    }

    public void mouseReleased(MouseEvent e) {
        down = false;
    }
    
}

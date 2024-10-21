package clients;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A class to display a picture in a client
 *
 * @author Mike Smith University of Brighton
 * @version 1.0
 */
public class Picture extends Canvas {
    private static final long serialVersionUID = 1;
    private int width = 260;
    private int height = 260;
    private Image thePicture = null;

    public Picture() {
        setSize(width, height);
    }

    public Picture(int aWidth, int aHeight) {
        width = aWidth;
        height = aHeight;
        setSize(width, height);
    }

    public void set(ImageIcon ic) {
        thePicture = ic.getImage();
        repaint();
    }

    public void clear() {
        thePicture = null;
        // Force repaint
        repaint();
    }

    public void paint(Graphics g) {
        drawImage((Graphics2D) g);
    }

    // Called by repaint
    public void update(Graphics g) {
        drawImage((Graphics2D) g);
    }

    /**
     * Draw the picture
     * First set the area to white and then
     * draw the image
     *
     * @param g Grapics context
     */
    public void drawImage(Graphics2D g) {
        setSize(width, height);
        g.setPaint(Color.white);
        g.fill(new Rectangle2D.Double(0, 0, width, height));
        if (thePicture != null) {
            g.drawImage(thePicture, 0, 0, null);
        }
    }
}

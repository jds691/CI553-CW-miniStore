package clients;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serial;

/**
 * A class to display a picture in a client
 *
 * @author Mike Smith University of Brighton
 * @version 1.0
 */
public class Picture extends Canvas {
    @Serial
    private static final long serialVersionUID = 1;

    private int width = 260;
    private int height = 260;
    private Image image = null;

    public Picture() {
        setSize(width, height);
    }

    public Picture(int aWidth, int aHeight) {
        width = aWidth;
        height = aHeight;
        setSize(width, height);
    }

    public void set(ImageIcon ic) {
        image = ic.getImage();
        repaint();
    }

    public void clear() {
        image = null;
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
        if (image != null) {
            g.drawImage(image, 0, 0, width, height, null);
        }
    }
}

package clients.customer;

import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */

public class CustomerView implements Observer {
    // Names of buttons
    class Name {
        public static final String CHECK = "Check";
        public static final String CLEAR = "Clear";
    }

    private static final int H = 300;
    private static final int W = 400;

    private final JLabel pageTitle = new JLabel();
    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtCheck = new JButton(Name.CHECK);
    private final JButton theBtClear = new JButton(Name.CLEAR);

    private final Picture thePicture = new Picture(80, 80);
    private StockReader theStock = null;
    private CustomerController cont = null;

    /**
     * Construct the view
     *
     * @param rpc Window in which to construct
     * @param mf  Factor to deliver order and stock objects
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public CustomerView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            // Database Access
            theStock = mf.makeStockReader();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // Content Pane
        Container cp = rpc.getContentPane();
        Container rootWindow = (Container) rpc;
        cp.setLayout(null);
        rootWindow.setSize(W, H);
        rootWindow.setLocation(x, y);

        Font f = new Font("Monospaced", Font.PLAIN, 12);

        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Search products");
        cp.add(pageTitle);

        // Check button
        theBtCheck.setBounds(16, 25, 80, 40);
        theBtCheck.addActionListener(
                e -> cont.doCheck(theInput.getText())
        );
        cp.add(theBtCheck);

        // Clear button
        theBtClear.setBounds(16, 25 + 60, 80, 40);
        theBtClear.addActionListener(
                e -> cont.doClear()
        );
        cp.add(theBtClear);

        // Message area
        theAction.setBounds(110, 25, 270, 20);
        theAction.setText("");
        cp.add(theAction);

        // Product no area
        theInput.setBounds(110, 50, 270, 40);
        theInput.setText("");
        cp.add(theInput);

        // Scrolling pane
        theSP.setBounds(110, 100, 270, 160);
        theOutput.setText("");
        theOutput.setFont(f);
        cp.add(theSP);
        theSP.getViewport().add(theOutput);

        // Picture area
        thePicture.setBounds(16, 25 + 60 * 2, 80, 80);
        cp.add(thePicture);
        thePicture.clear();

        rootWindow.setVisible(true);
        theInput.requestFocus();
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     *
     * @param c The controller
     */
    public void setController(CustomerController c) {
        cont = c;
    }

    /**
     * Update the view
     *
     * @param modelC The observed model
     * @param arg    Specific args
     */
    public void update(Observable modelC, Object arg) {
        CustomerModel model = (CustomerModel) modelC;
        String message = (String) arg;
        theAction.setText(message);
        ImageIcon image = model.getPicture();

        if (image == null) {
            thePicture.clear();
        } else {
            thePicture.set(image);
        }

        theOutput.setText(model.getBasket().getDetails());
        theInput.requestFocus();
    }
}

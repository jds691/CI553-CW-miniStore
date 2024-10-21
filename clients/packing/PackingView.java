package clients.packing;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Packing view.
 */

public class PackingView implements Observer {
    private static final String PACKED = "Packed";

    private static final int H = 300;
    private static final int W = 400;

    private final JLabel pageTitle = new JLabel();
    private final JLabel theAction = new JLabel();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtPack = new JButton(PACKED);

    private OrderProcessing theOrder = null;

    private PackingController cont = null;

    /**
     * Construct the view
     *
     * @param rpc Window in which to construct
     * @param mf  Factor to deliver order and stock objects
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public PackingView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            // Process order
            theOrder = mf.makeOrderProcessing();
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
        pageTitle.setText("Packing Bought Order");
        cp.add(pageTitle);

        // Check Button
        theBtPack.setBounds(16, 25, 80, 40);
        theBtPack.addActionListener(
                e -> cont.doPacked()
        );
        cp.add(theBtPack);

        // Message area
        theAction.setBounds(110, 25, 270, 20);
        theAction.setText("");
        cp.add(theAction);

        // Scrolling pane
        theSP.setBounds(110, 55, 270, 205);
        theOutput.setText("");
        theOutput.setFont(f);
        cp.add(theSP);

        theSP.getViewport().add(theOutput);
        rootWindow.setVisible(true);
    }

    public void setController(PackingController c) {
        cont = c;
    }

    /**
     * Update the view
     *
     * @param modelC The observed model
     * @param arg    Specific args
     */
    @Override
    public void update(Observable modelC, Object arg) {
        PackingModel model = (PackingModel) modelC;
        String message = (String) arg;
        theAction.setText(message);

        Basket basket = model.getBasket();
        if (basket != null) {
            theOutput.setText(basket.getDetails());
        } else {
            theOutput.setText("");
        }
    }
}


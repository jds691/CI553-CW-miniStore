package clients.backDoor;

import middle.MiddleFactory;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */

public class BackDoorView implements Observer {
    private static final String RESTOCK = "Add";
    private static final String CLEAR = "Clear";
    private static final String QUERY = "Query";

    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width  of window pixels

    private final JLabel pageTitle = new JLabel();
    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextField theInputNo = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtClear = new JButton(CLEAR);
    private final JButton theBtRStock = new JButton(RESTOCK);
    private final JButton theBtQuery = new JButton(QUERY);

    private StockReadWriter theStock = null;
    private BackDoorController cont = null;

    /**
     * Construct the view
     *
     * @param rpc Window in which to construct
     * @param mf  Factor to deliver order and stock objects
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public BackDoorView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            // Database access
            theStock = mf.makeStockReadWriter();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        // Content Pane
        Container cp = rpc.getContentPane();
        // Root Window
        Container rootWindow = (Container) rpc;
        // No layout manager
        cp.setLayout(null);
        // Size of Window
        rootWindow.setSize(W, H);
        rootWindow.setLocation(x, y);

        Font f = new Font("Monospaced", Font.PLAIN, 12);  // Font f is

        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Staff check and manage stock");
        cp.add(pageTitle);

        // Buy button
        theBtQuery.setBounds(16, 25, 80, 40);
        theBtQuery.addActionListener(
                e -> cont.doQuery(theInput.getText())
        );
        cp.add(theBtQuery);

        // Check Button
        theBtRStock.setBounds(16, 25 + 60, 80, 40);
        theBtRStock.addActionListener(
                e -> cont.doRStock(theInput.getText(), theInputNo.getText())
        );
        cp.add(theBtRStock);

        // Buy button
        theBtClear.setBounds(16, 25 + 60 * 2, 80, 40);
        theBtClear.addActionListener(
                e -> cont.doClear()
        );
        cp.add(theBtClear);

        // Message area
        theAction.setBounds(110, 25, 270, 20);
        theAction.setText("");
        cp.add(theAction);

        // Input Area
        theInput.setBounds(110, 50, 120, 40);
        theInput.setText("");
        cp.add(theInput);

        // Input Area
        theInputNo.setBounds(260, 50, 120, 40);
        theInputNo.setText("0");
        cp.add(theInputNo);

        // Scrolling pane
        theSP.setBounds(110, 100, 270, 160);
        theOutput.setText("");
        theOutput.setFont(f);
        cp.add(theSP);

        theSP.getViewport().add(theOutput);
        rootWindow.setVisible(true);
        theInput.requestFocus();
    }

    public void setController(BackDoorController c) {
        cont = c;
    }

    /**
     * Update the view, called by notifyObservers(theAction) in model,
     *
     * @param modelC The observed model
     * @param arg    Specific args
     */
    @Override
    public void update(Observable modelC, Object arg) {
        BackDoorModel model = (BackDoorModel) modelC;
        String message = (String) arg;
        theAction.setText(message);

        theOutput.setText(model.getBasket().getDetails());
        theInput.requestFocus();
    }

}
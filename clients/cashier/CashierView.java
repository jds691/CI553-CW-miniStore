package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;


/**
 * View of the model
 */
public class CashierView implements Observer {
    // Height of window pixels
    private static final int H = 300;
    // Width  of window pixels
    private static final int W = 400;

    private static final String CHECK = "Check";
    private static final String BUY = "Buy";
    private static final String BOUGHT = "Bought/Pay";

    private final JLabel pageTitle = new JLabel();
    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtCheck = new JButton(CHECK);
    private final JButton theBtBuy = new JButton(BUY);
    private final JButton theBtBought = new JButton(BOUGHT);

    private StockReadWriter theStock = null;
    private OrderProcessing theOrder = null;
    private CashierController cont = null;

    /**
     * Construct the view
     *
     * @param rpc Window in which to construct
     * @param mf  Factor to deliver order and stock objects
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public CashierView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            // Database access
            theStock = mf.makeStockReadWriter();
            // Process order
            theOrder = mf.makeOrderProcessing();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // Content Pane
        Container cp = rpc.getContentPane();
        Container rootWindow = (Container) rpc;
        // No layout manager
        cp.setLayout(null);
        rootWindow.setSize(W, H);
        rootWindow.setLocation(x, y);

        Font f = new Font("Monospaced", Font.PLAIN, 12);

        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Thank You for Shopping at MiniStrore");
        cp.add(pageTitle);

        // Check Button
        theBtCheck.setBounds(16, 25, 80, 40);
        theBtCheck.addActionListener(
                e -> cont.doCheck(theInput.getText())
        );
        cp.add(theBtCheck);

        // Buy button
        theBtBuy.setBounds(16, 25 + 60, 80, 40);
        theBtBuy.addActionListener(
                e -> cont.doBuy()
        );
        cp.add(theBtBuy);

        // Bought Button
        theBtBought.setBounds(16, 25 + 60 * 3, 80, 40);
        theBtBought.addActionListener(
                e -> cont.doBought()
        );
        cp.add(theBtBought);

        // Message area
        theAction.setBounds(110, 25, 270, 20);
        theAction.setText("");
        cp.add(theAction);

        // Input Area
        theInput.setBounds(110, 50, 270, 40);
        theInput.setText("");
        cp.add(theInput);

        // Scrolling pane
        theSP.setBounds(110, 100, 270, 160);
        theOutput.setText("");
        theOutput.setFont(f);
        cp.add(theSP);

        theSP.getViewport().add(theOutput);
        rootWindow.setVisible(true);
        theInput.requestFocus();
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     *
     * @param c The controller
     */
    public void setController(CashierController c) {
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
        CashierModel model = (CashierModel) modelC;
        String message = (String) arg;
        theAction.setText(message);
        Basket basket = model.getBasket();
        if (basket == null)
            theOutput.setText("Customers order");
        else
            theOutput.setText(basket.getDetails());

        theInput.requestFocus();               // Focus is here
    }
}

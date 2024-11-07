package clients.cashier;

import logic.Order;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * View of the model
 */
public class CashierView implements Observer {
    private static final int HEIGHT = 300;
    private static final int WIDTH = 400;

    private static final String CHECK = "Check";
    private static final String BUY = "Buy";
    private static final String BOUGHT = "Bought/Pay";

    private final JLabel pageTitle = new JLabel();
    private final JLabel promptLabel = new JLabel();
    private final JTextField productNumberInput = new JTextField();
    private final JTextArea messageOutput = new JTextArea();
    private final JScrollPane messageScrollPane = new JScrollPane();
    private final JButton checkButton = new JButton(CHECK);
    private final JButton buyButton = new JButton(BUY);
    private final JButton payButton = new JButton(BOUGHT);

    private CashierController controller = null;

    /**
     * Construct the view
     *
     * @param rootPane Window in which to construct
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public CashierView(RootPaneContainer rootPane, int x, int y) {
        Container contentPane = rootPane.getContentPane();
        Container rootWindow = (Container) rootPane;
        // No layout manager
        contentPane.setLayout(null);
        rootWindow.setSize(WIDTH, HEIGHT);
        rootWindow.setLocation(x, y);

        Font monospaceFont = new Font("Monospaced", Font.PLAIN, 12);

        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Thank You for Shopping at MiniStore");
        contentPane.add(pageTitle);

        checkButton.setBounds(16, 25, 80, 40);
        checkButton.addActionListener(
                e -> controller.queryProduct(productNumberInput.getText())
        );
        contentPane.add(checkButton);

        buyButton.setBounds(16, 25 + 60, 80, 40);
        buyButton.addActionListener(
                e -> controller.buyCurrentProduct()
        );
        contentPane.add(buyButton);

        payButton.setBounds(16, 25 + 60 * 3, 80, 40);
        payButton.addActionListener(
                e -> controller.buyBasket()
        );
        contentPane.add(payButton);

        promptLabel.setBounds(110, 25, 270, 20);
        promptLabel.setText("");
        contentPane.add(promptLabel);

        productNumberInput.setBounds(110, 50, 270, 40);
        productNumberInput.setText("");
        contentPane.add(productNumberInput);

        messageScrollPane.setBounds(110, 100, 270, 160);
        messageOutput.setText("");
        messageOutput.setFont(monospaceFont);
        contentPane.add(messageScrollPane);

        messageScrollPane.getViewport().add(messageOutput);
        rootWindow.setVisible(true);
        productNumberInput.requestFocus();
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     *
     * @param controller The controller
     */
    public void setController(CashierController controller) {
        this.controller = controller;
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
        promptLabel.setText(message);
        Order order = model.getCurrentOrder();
        if (order == null)
            messageOutput.setText("Customers order");
        else
            messageOutput.setText(order.getRichDescription());

        productNumberInput.requestFocus();
    }
}

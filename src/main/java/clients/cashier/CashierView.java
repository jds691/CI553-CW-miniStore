package clients.cashier;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View of the model
 */
public class CashierView implements PropertyChangeListener {
    private static final int HEIGHT = 300;
    private static final int WIDTH = 400;

    private static final String CHECK = "Check";
    private static final String BUY = "Buy";
    private static final String BOUGHT = "Bought/Pay";

    private final JLabel productPromptLabel = new JLabel("Product Name/ID");
    private final JTextField productNumberInput = new JTextField();
    private final JButton checkButton = new JButton(CHECK);

    private final JLabel quantityPromptLabel = new JLabel("Quantity");
    private SpinnerNumberModel quantityInputModel;
    private final JSpinner quantityInput = new JSpinner();
    private final JButton addButton = new JButton("Add");

    private final JLabel quantityLabel = new JLabel("0 Items");
    private final JPanel scrollPaneViewport;
    private final JScrollPane scrollPane;

    private final JButton buyButton = new JButton("Buy");
    private final JButton clearAllButton = new JButton("Clear All");

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

        productPromptLabel.setBounds(16, 7, 120, 20);
        contentPane.add(productPromptLabel);

        productNumberInput.setBounds(16, 32, 120, 40);
        productNumberInput.setText("");
        contentPane.add(productNumberInput);

        checkButton.setBounds(146, 32, 80, 40);
        checkButton.addActionListener(
                e -> controller.queryProduct(productNumberInput.getText())
        );
        contentPane.add(checkButton);

        quantityPromptLabel.setBounds(237, 7, 62, 20);
        contentPane.add(quantityPromptLabel);

        quantityInput.setBounds(237, 32, 62, 40);
        quantityInput.setModel(new SpinnerNumberModel(1, 1, null, 1));
        quantityInput.setEnabled(false);
        contentPane.add(quantityInput);

        addButton.setBounds(304, 32, 80, 40);
        //addButton.addActionListener();
        contentPane.add(addButton);

        quantityLabel.setBounds(16, 80, 368, 20);
        contentPane.add(quantityLabel);


        scrollPaneViewport = new JPanel(new GridLayout(0, 1, 1, 1));

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.add(scrollPaneViewport, BorderLayout.PAGE_START);

        scrollPane = new JScrollPane(outerPanel);
        scrollPane.setBounds(16, 100, 368, 120);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(scrollPane);

        buyButton.setBounds(300, 230, 80, 30);
        /*buyButton.addActionListener(
                e -> controller.buyCurrentProduct()
        );*/
        contentPane.add(buyButton);

        clearAllButton.setBounds(16, 230, 80, 30);
        /*clearAllButton.addActionListener(
                e -> controller.buyCurrentProduct()
        );*/
        contentPane.add(clearAllButton);

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
        this.controller.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
         /*CashierModel model = (CashierModel) modelC;
        String message = (String) arg;
        promptLabel.setText(message);
        Order order = model.getCurrentOrder();
        if (order == null)
            messageOutput.setText("Customers order");
        else
            messageOutput.setText(controller.getOrderDescription());

        productNumberInput.requestFocus();*/

        String property = evt.getPropertyName();

        switch (property) {
            case CashierModel.Property.PROMPT:
                break;
            case CashierModel.Property.STATE:
                break;
            default:
                break;
                //throw new IllegalStateException("Unexpected value: " + property);
        }
    }
}

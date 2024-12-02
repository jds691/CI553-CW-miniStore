package clients.packing;

import logic.Order;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

import static logic.Order.Item;

/**
 * Implements the Packing view.
 */

public class PackingView implements Observer {
    private static final String PACKED = "Packed";

    private static final int HEIGHT = 300;
    private static final int WIDTH = 400;

    private final JLabel pageTitle = new JLabel();
    private final JLabel promptLabel = new JLabel();
    private final JScrollPane messageScrollPane = new JScrollPane();
    private final JButton packButton = new JButton(PACKED);

    private PackingController controller = null;

    /**
     * Construct the view
     *
     * @param rootPane Window in which to construct
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public PackingView(RootPaneContainer rootPane, int x, int y) {
        // Content Pane
        Container contentPane = rootPane.getContentPane();
        Container rootWindow = (Container) rootPane;
        contentPane.setLayout(null);
        rootWindow.setSize(WIDTH, HEIGHT);
        rootWindow.setLocation(x, y);

        Font monospaceFont = new Font("Monospaced", Font.PLAIN, 12);

        pageTitle.setBounds(16, 0, 368, 20);
        pageTitle.setText("Packing Bought Order");
        contentPane.add(pageTitle);

        /*packButton.setBounds(16, 25, 80, 40);
        packButton.addActionListener(
                e -> controller.packOrder()
        );
        contentPane.add(packButton);*/

        promptLabel.setBounds(16, 25, 368, 20);
        promptLabel.setText("");
        contentPane.add(promptLabel);

        messageScrollPane.setBounds(16, 55, 368, 205);
        contentPane.add(messageScrollPane);

        rootWindow.setVisible(true);
    }

    public void setController(PackingController controller) {
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
        PackingModel model = (PackingModel) modelC;
        String message = (String) arg;
        promptLabel.setText(message);

        Order order = model.getCurrentOrder();

        messageScrollPane.getViewport().removeAll();
        if (order != null) {

            JComponent cachedRow = createOrderRow(order);

            messageScrollPane.getViewport().add(cachedRow);
            messageScrollPane.getViewport().add(cachedRow);
            messageScrollPane.getViewport().add(cachedRow);
            messageScrollPane.getViewport().add(cachedRow);
            messageScrollPane.getViewport().add(cachedRow);
            messageScrollPane.getViewport().add(cachedRow);

            //messageOutput.setText(controller.getOrderDescription());
        } else {
            //messageOutput.setText("");
        }
    }

    /**
     * Creates the Java Swing view to represent an order in the GUI.
     */
    private JComponent createOrderRow(Order order) {
        JPanel orderRowPanel = new JPanel(new GridBagLayout());
        // 368 taken from messageScrollPane width
        orderRowPanel.setMinimumSize(new Dimension(368, 0));
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.ipadx = 8;
        constraints.anchor = GridBagConstraints.CENTER;

        JLabel orderNumberLabel = new JLabel(String.format("#%s", order.getOrderNumber()));
        Font orderNumberFont = new Font("Dialog", Font.BOLD, 18);
        orderNumberLabel.setFont(orderNumberFont);

        orderRowPanel.add(orderNumberLabel, constraints);

        Item[] items = order.getAllItems();
        int itemCount = 0;
        for (Item item : items) {
            itemCount += item.getQuantity();
        }

        JLabel itemCountLabel = new JLabel(
                itemCount == 1 ? "1 Item" : String.format("%d Items", itemCount)
        );

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.ipadx = 0;
        constraints.anchor = GridBagConstraints.LINE_START;

        orderRowPanel.add(itemCountLabel, constraints);

        JLabel costLabel = new JLabel(
                String.format("£%.2f", controller.getOrderCost(order))
        );

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.ipadx = 0;
        constraints.anchor = GridBagConstraints.LINE_START;

        orderRowPanel.add(costLabel, constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.ipadx = 0;
        constraints.anchor = GridBagConstraints.LINE_END;

        JComboBox stateComboBox = new JComboBox(Order.State.values());
        stateComboBox.setSelectedItem(order.getState().ordinal());
        //TODO: Add action listener

        orderRowPanel.add(stateComboBox, constraints);

        return orderRowPanel;
    }
}


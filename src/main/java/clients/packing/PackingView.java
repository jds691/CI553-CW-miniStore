package clients.packing;

import logic.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import static javax.swing.JOptionPane.showMessageDialog;
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
    private final JPanel orderListViewport;
    private final JScrollPane messageScrollPane;
    private final JButton packButton = new JButton(PACKED);

    private PackingController controller = null;

    /**
     * Construct the view
     *
     * @param rootPane Window in which to construct
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public PackingView(RootPaneContainer rootPane, PackingController controller, int x, int y) {
        this.controller = controller;
        // Content Pane
        Container contentPane = rootPane.getContentPane();
        Container rootWindow = (Container) rootPane;
        contentPane.setLayout(null);
        rootWindow.setSize(WIDTH, HEIGHT);
        rootWindow.setLocation(x, y);

        Font monospaceFont = new Font("Monospaced", Font.PLAIN, 12);

        Font pageTitleFont = new Font("Dialog", Font.BOLD, 18);
        pageTitle.setBounds(16, 0, 368, 20);
        pageTitle.setText("All Orders");
        pageTitle.setFont(pageTitleFont);
        contentPane.add(pageTitle);

        /*packButton.setBounds(16, 25, 80, 40);
        packButton.addActionListener(
                e -> controller.packOrder()
        );
        contentPane.add(packButton);*/

        promptLabel.setBounds(16, 25, 368, 20);
        contentPane.add(promptLabel);

        orderListViewport = new JPanel(new GridLayout(0, 1, 1, 1));

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.add(orderListViewport, BorderLayout.PAGE_START);

        // This sets the initial view of the viewport not what the viewport itself is
        // Find a way to change the contents of the viewport view AND have the viewport update
        messageScrollPane = new JScrollPane(outerPanel);
        messageScrollPane.setBounds(16, 55, 368, 205);
        messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentPane.add(messageScrollPane);

        Order[][] orders = controller.getAllOrders();
        renderOrderListViewport(orders);

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

        if (arg instanceof String message) {
            showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Order[][] orders = model.getAllOrders();
        renderOrderListViewport(orders);
    }

    /**
     * Removes all controls from the orderListViewport and adds all of them in from the OrderProcessor
     */
    private void renderOrderListViewport(Order[][] orders) {
        if (orders == null) {
            promptLabel.setText("An error has occurred.");
            return;
        }

        int totalOrders = 0;

        orderListViewport.removeAll();
        for (Order[] orderCollection : orders) {
            for (Order order : orderCollection) {
                totalOrders++;

                JComponent row = createOrderRow(order);

                if (row == null) {
                    promptLabel.setText("Unable to connect to server");
                    orderListViewport.removeAll();
                    orderListViewport.revalidate();
                    orderListViewport.repaint();
                    return;
                }

                orderListViewport.add(row);
            }
        }

        promptLabel.setText(totalOrders == 1 ? "1 Order" : totalOrders + " Orders");

        orderListViewport.revalidate();
        orderListViewport.repaint();
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

        double orderCost = controller.getOrderCost(order);

        if (orderCost == -1) {
            return null;
        }

        JLabel costLabel = new JLabel(
                String.format("£%.2f", orderCost)
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

        String[] states = new String[] { "Waiting", "Being Packed", "To Be Collected" };
        JComboBox stateComboBox = new JComboBox(states);
        stateComboBox.setSelectedIndex(order.getState().ordinal());
        stateComboBox.addActionListener((actionEvent) -> {
            order.setState(Order.State.values()[stateComboBox.getSelectedIndex()]);
            controller.updateOrderState(order);
        });

        orderRowPanel.add(stateComboBox, constraints);
        orderRowPanel.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            createFrameForOrder(order);
                        }
                    }
                }
        );

        return orderRowPanel;
    }

    private void createFrameForOrder(Order order) {

        PackingOrderModel packingOrderModel = new PackingOrderModel(this.controller.getFactory());
        PackingOrderController packingOrderController = new PackingOrderController(packingOrderModel);

        JFrame orderFrame = new PackingOrderFrame(order, packingOrderController);
        orderFrame.setVisible(true);
    }
}


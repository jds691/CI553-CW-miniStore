package clients.packing;

import clients.Picture;
import logic.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

import static javax.swing.JOptionPane.showMessageDialog;

public class PackingOrderFrame extends JFrame {
    private static final int HEIGHT = 300;
    private static final int WIDTH = 400;

    private final Order order;
    private final PackingOrderController controller;

    private JPanel itemListViewport;

    public PackingOrderFrame(Order order, PackingOrderController controller) {
        super();

        this.order = order;
        this.controller = controller;

        setTitle(String.format("Order #%s", order.getOrderNumber()));
        // Do not quit when this window is closed. Just dispose of it
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        buildView();
    }

    private void buildView() {
        setLayout(null);
        setSize(WIDTH, HEIGHT);

        JComponent header = createFrameHeader();
        header.setBounds(16, 5, 368, 45);
        add(header);

        itemListViewport = new JPanel(new GridLayout(0, 1, 1, 1));

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.add(itemListViewport, BorderLayout.PAGE_START);

        JScrollPane itemsScrollPane = new JScrollPane(outerPanel);
        itemsScrollPane.setBounds(16, 55, 368, 205);
        itemsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        itemsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(itemsScrollPane);

        for (Order.Item item  : order.getAllItems()) {
            itemListViewport.add(createOrderItemRow(item));
        }

        itemListViewport.revalidate();
        itemListViewport.repaint();
    }

    private JComponent createFrameHeader() {
        JPanel orderRowPanel = new JPanel(new GridBagLayout());
        // 368 taken from messageScrollPane width
        orderRowPanel.setMinimumSize(new Dimension(368, 45));
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

        Order.Item[] items = order.getAllItems();
        int itemCount = 0;
        for (Order.Item item : items) {
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

        JLabel costLabel = null;
        try {
            costLabel = new JLabel(
                    String.format("£%.2f", controller.getOrderCost(order))
            );
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            showMessageDialog(null, "Unable to connect to server", "Error", JOptionPane.ERROR_MESSAGE);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

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
            try {
                controller.updateOrderState(order);
            } catch (Exception e) {
                System.err.println("Exception: " + e.getMessage());
                showMessageDialog(null, "Unable to connect to server", "Error", JOptionPane.ERROR_MESSAGE);
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        });

        orderRowPanel.add(stateComboBox, constraints);

        return orderRowPanel;
    }

    private JComponent createOrderItemRow(Order.Item item) {
        JPanel itemRowPanel = new JPanel(new GridBagLayout());
        itemRowPanel.setMinimumSize(new Dimension(368, 0));
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.ipadx = 8;
        constraints.anchor = GridBagConstraints.CENTER;

        Picture itemIcon = new Picture(42, 42);
        itemIcon.clear();
        try {
            itemIcon.set(controller.getItemIcon(item));
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            showMessageDialog(null, "Unable to connect to server", "Error", JOptionPane.ERROR_MESSAGE);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

        itemRowPanel.add(itemIcon, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.ipadx = 0;
        constraints.anchor = GridBagConstraints.LINE_START;

        Font boldFont = new Font("Dialog", Font.BOLD, 14);

        JLabel productNameLabel = null;
        try {
            productNameLabel = new JLabel(
                    controller.getProductName(item)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        productNameLabel.setFont(boldFont);

        itemRowPanel.add(productNameLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.ipadx = 0;
        constraints.anchor = GridBagConstraints.LINE_START;

        JLabel quantityLabel = new JLabel(
                String.format("Quantity: %d", item.getQuantity())
        );

        itemRowPanel.add(quantityLabel, constraints);

        itemIcon.invalidate();
        itemIcon.repaint();

        itemRowPanel.invalidate();
        itemRowPanel.repaint();

        return itemRowPanel;
    }
}

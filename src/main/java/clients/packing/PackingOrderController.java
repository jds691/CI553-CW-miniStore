package clients.packing;

import logic.Order;

import javax.swing.*;

public class PackingOrderController {
    private final PackingOrderModel model;

    public PackingOrderController(PackingOrderModel model) {
        this.model = model;
    }

    public double getOrderCost(Order order) throws Exception {
        return model.getOrderCost(order);
    }

    public void updateOrderState(Order order) throws Exception {
        model.updateOrderState(order);
    }

    public ImageIcon getItemIcon(Order.Item item) throws Exception {
        return model.getImageIcon(item);
    }

    public String getProductName(Order.Item item) throws Exception {
        return model.getProductName(item);
    }
}

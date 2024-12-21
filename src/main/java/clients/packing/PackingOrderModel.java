package clients.packing;

import logic.*;

import javax.swing.*;

public class PackingOrderModel {
    private final ProductReader productReader;
    private final OrderProcessor orderProcessor;

    public PackingOrderModel(LogicFactory factory) {
        productReader = factory.getProductReader();
        orderProcessor = factory.getOrderProcessor();
    }

    public double getOrderCost(Order order) {
        double total = 0.0;

        for (Order.Item item : order.getAllItems()) {
            Product product = productReader.getProductDetails(item.getProductNumber());

            total += product.getPrice() * item.getQuantity();
        }

        return total;
    }

    public void updateOrderState(Order order) {
        orderProcessor.addOrderToQueue(order);
    }

    public ImageIcon getImageIcon(Order.Item item) {
        return productReader.getProductImage(item.getProductNumber());
    }

    public String getProductName(Order.Item item) {
        return productReader.getProductDetails(item.getProductNumber()).getDescription();
    }
}

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

    public double getOrderCost(Order order) throws Exception {
        double total = 0.0;

        for (Order.Item item : order.getAllItems()) {
            Product product = productReader.getProductDetails(item.getProductNumber());

            total += product.getPrice() * item.getQuantity();
        }

        return total;
    }

    public void updateOrderState(Order order) throws Exception {
        orderProcessor.addOrderToQueue(order);
    }

    public ImageIcon getImageIcon(Order.Item item) throws Exception {
        return productReader.getProductImage(item.getProductNumber());
    }

    public String getProductName(Order.Item item) throws Exception {
        return productReader.getProductDetails(item.getProductNumber()).getName();
    }
}

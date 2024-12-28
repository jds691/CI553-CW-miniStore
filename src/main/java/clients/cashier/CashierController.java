package clients.cashier;

import logic.Order;
import logic.Product;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * The Cashier Controller
 */
public class CashierController {
    private final CashierModel model;

    /**
     * Constructor
     *
     * @param model The model
     */
    public CashierController(CashierModel model) {
        this.model = model;
    }

    /**
     * Check interaction from view
     *
     * @param productNumber The product number to be checked
     */
    public void queryProduct(String productNumber) {
        model.queryProduct(productNumber);
    }

    /**
     * Buy interaction from view
     */
    public void buyCurrentProduct(int quantity) {
        Order order = getCurrentOrder();

        if (order != null) {
            Order.Item item = order.getItem(getCurrentProduct().getProductNumber());

            // Update item as long as we don't exceed max quantity
            if (item.getQuantity() + quantity <= getCurrentProduct().getQuantity()) {
                item.setQuantity(item.getQuantity() + quantity);
                updateOrderItem(item, true);
            }
            return;
        }

        model.buyCurrentProduct(quantity);
    }

    public Order getCurrentOrder() {
        return model.getCurrentOrder();
    }

    public void updateOrderItem(Order.Item item) {
        updateOrderItem(item, false);
    }

    public void updateOrderItem(Order.Item item, boolean automatic) {
        model.updateOrderItem(item);
    }

    public void removeOrderItem(Order.Item item) {
        model.removeOrderItem(item);
    }

    public Product getCurrentProduct() {
        return model.getCurrentProduct();
    }

    public void clearCurrentOrder() {
        model.clearCurrentOrder();
    }

    /**
     * Bought interaction from view
     */
    public void buyBasket() {
        model.buyBasket();
        clearCurrentOrder();
    }

    public String getOrderDescription() {
        return model.getOrderDescription();
    }

    public ImageIcon getItemIcon(Order.Item item) {
        return model.getItemIcon(item);
    }

    public String getProductName(Order.Item item) {
        return model.getProductName(item);
    }

    public int getProductQuantity(String productNumber) {
        return model.getProductQuantity(productNumber);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        model.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        model.removePropertyChangeListener(listener);
    }
}

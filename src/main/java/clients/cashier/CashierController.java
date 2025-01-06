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
     * Determines whether the last prompt output by the underlying model was an error message or not
     *
     * @return Was error message
     */
    public boolean getWasLastPromptError() {
        return model.getWasLastPromptError();
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

    /**
     * Get the current order being edited
     *
     * @return Order being edited
     */
    public Order getCurrentOrder() {
        return model.getCurrentOrder();
    }

    /**
     * Updates an items quantity inside the order if it is already contained.
     *
     * @param item Item to update quantity of
     */
    public void updateOrderItem(Order.Item item) {
        updateOrderItem(item, false);
    }

    /**
     * Updates an items quantity inside the order if it is already contained.
     *
     * @param item Item to update quantity of
     * @param automatic Whether an update was performed on behalf of the user e.g. Adding the same item twice
     */
    public void updateOrderItem(Order.Item item, boolean automatic) {
        model.updateOrderItem(item);
    }

    /**
     * Removes the specified item from the order
     *
     * @param item Item to remove
     */
    public void removeOrderItem(Order.Item item) {
        model.removeOrderItem(item);
    }

    /**
     * Gets the last product checked by the user
     *
     * @return Last product checked
     */
    public Product getCurrentProduct() {
        return model.getCurrentProduct();
    }

    /**
     * Clears the current order and resets the model and view
     */
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

    /**
     * Gets the quantity of the specified item currently in stock
     *
     * @param productNumber Product number of product to check
     * @return Quantity in stock
     */
    public int getProductQuantity(String productNumber) {
        return model.getProductQuantity(productNumber);
    }

    /**
     * Returns the image of a product
     *
     * @param item Item that the product is associated with
     * @return Image to display in a {@link clients.Picture}
     */
    public ImageIcon getItemIcon(Order.Item item) {
        return model.getItemIcon(item);
    }

    /**
     * Returns the name of a product
     *
     * @param item Item that the product is associated with
     * @return Name of product
     */
    public String getProductName(Order.Item item) {
        return model.getProductName(item);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        model.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        model.removePropertyChangeListener(listener);
    }
}

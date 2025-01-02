package clients.cashier;

import clients.adapters.ProductNameAdapter;
import debug.DEBUG;
import logic.*;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;

/**
 * Implements the Model of the cashier client
 */
public class CashierModel {
    /**
     * Current state of the model
     */
    private State currentState;
    // Current product
    private Product currentProduct = null;
    // Bought items
    private Order currentOrder = null;
    private ProductReader productReader = null;
    // Database remote.access
    private StockWriter stockWriter = null;
    // Process order
    private OrderProcessor orderProcessor = null;
    private ProductNameAdapter productNameAdapter = null;

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Construct the model of the Cashier
     *
     * @param factory The factory to create the connection objects
     */
    public CashierModel(LogicFactory factory) {
        try {
            productReader = factory.getProductReader();
            stockWriter = factory.getStockWriter();
            orderProcessor = factory.getOrderProcessor();
            productNameAdapter = new ProductNameAdapter(productReader.getRepository());
        } catch (Exception e) {
            DEBUG.error("CashierModel.constructor\n%s", e.getMessage());
        }

        currentState = State.PROCESS;
    }

    /**
     * Get the current order being edited
     *
     * @return Order being edited
     */
    public Order getCurrentOrder() {
        return currentOrder;
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
        if (currentOrder != null && currentOrder.getItem(item.getProductNumber()) != null) {
            currentOrder.updateItem(item);

            propertyChangeSupport.firePropertyChange(Property.ORDER_CONTENTS, null, currentOrder);

            if (automatic) {
                // Called by add button instead of OrderItemEditRow
                currentState = State.PROCESS;
                propertyChangeSupport.firePropertyChange(Property.STATE, null, currentState);
            }
        }
    }

    /**
     * Removes the specified item from the order
     *
     * @param item Item to remove
     */
    public void removeOrderItem(Order.Item item) {
        if (currentOrder != null && currentOrder.getItem(item.getProductNumber()) != null) {
            currentOrder.removeItem(item);

            propertyChangeSupport.firePropertyChange(Property.ORDER_CONTENTS, null, currentOrder);
        }
    }

    /**
     * Gets the last product checked by the user
     *
     * @return Last product checked
     */
    public Product getCurrentProduct() {
        return currentProduct;
    }

    /**
     * Clears the current order and resets the model and view
     */
    public void clearCurrentOrder() {
        currentOrder = null;
        currentState = State.PROCESS;

        propertyChangeSupport.firePropertyChange(Property.STATE, null, currentState);
        propertyChangeSupport.firePropertyChange(Property.ORDER_CONTENTS, null, null);
    }

    /**
     * Queries the product's description, price and quantity and outputs it to observers.
     *
     * @param productNumber The product number of the item
     */
    public void queryProduct(String productNumber) {
        String prompt;
        currentState = State.PROCESS;

        // Product being processed
        productNumber = productNumber.trim();
        int amount = 1;

        // productNumber must be only numbers and at least 1
        if (!productNumber.matches("^[0-9]+$")) {
            productNumber = productNameAdapter.getProductNumber(productNumber);
        }

        if (productReader.doesProductExist(productNumber)) {
            Product product = productReader.getProductDetails(productNumber);
            if (product.getQuantity() >= amount) {
                prompt = String.format(
                        "%s : %7.2f (%2d) ",
                        product.getName(),
                        product.getPrice(),
                        product.getQuantity()
                );

                //   Remember prod.
                currentProduct = product;
                //   OK await BUY
                currentState = State.CHECKED;
                propertyChangeSupport.firePropertyChange(Property.STATE, null, currentState);
            } else {
                prompt = product.getName() + " not in stock";
                propertyChangeSupport.firePropertyChange(Property.STATE, null, currentState);
            }
        } else {
            prompt = "Unknown product number " + productNumber;
            propertyChangeSupport.firePropertyChange(Property.STATE, null, currentState);
        }

        propertyChangeSupport.firePropertyChange(Property.PROMPT, null, prompt);
    }

    /**
     * Buy the product
     */
    public void buyCurrentProduct(int quantity) {
        String prompt;

        if (currentState != State.CHECKED) {
            prompt = "please check its availability";
        } else {
            //TODO: WHY DO WE DIRECTLY BUY STOCK BEFORE THE ORDER IS BOUGHT???
            boolean stockBought = stockWriter.buyStock(currentProduct, quantity);

            if (stockBought) {
                makeBasketIfRequired();
                currentOrder.addItem(new Order.Item(currentProduct.getProductNumber(), quantity));
                propertyChangeSupport.firePropertyChange(Property.ORDER_CONTENTS, null, currentOrder);
                prompt = "Purchased " + currentProduct.getName();
            } else {
                prompt = "!!! Not in stock";
            }
        }

        // Return to State.PROCESS when done
        currentState = State.PROCESS;
        propertyChangeSupport.firePropertyChange(Property.STATE, null, currentState);
        propertyChangeSupport.firePropertyChange(Property.PROMPT, null, prompt);
    }

    /**
     * Customer pays for the contents of the basket
     */
    public void buyBasket() {
        String prompt;

        if (currentOrder != null && !currentOrder.isEmpty()) {
            orderProcessor.addOrderToQueue(currentOrder);
        }

        prompt = "Start New Order";
        currentState = State.PROCESS;
        propertyChangeSupport.firePropertyChange(Property.STATE, null, currentState);

        currentOrder = null;
        propertyChangeSupport.firePropertyChange(Property.ORDER_CONTENTS, null, null);

        propertyChangeSupport.firePropertyChange(Property.PROMPT, null, prompt);
    }

    /**
     * ask for update of view called at start of day
     * or after system reset
     */
    public void askForUpdate() {
        propertyChangeSupport.firePropertyChange(Property.PROMPT, null, "Welcome!");
    }

    //TODO: See if theres a way to make a unified source for this
    public String getOrderDescription() {
        Locale locale = Locale.UK;
        StringBuilder stringBuilder = new StringBuilder(256);
        Formatter formatter = new Formatter(stringBuilder, locale);
        String currencySymbol = (Currency.getInstance(locale)).getSymbol();
        double total = 0.00;

        if (currentOrder.getOrderNumber() != 0)
            formatter.format("Order number: %03d\n", currentOrder.getOrderNumber());

        if (!currentOrder.isEmpty()) {
            for (Order.Item item : currentOrder.getAllItems()) {
                Product product = productReader.getProductDetails(item.getProductNumber());
                formatter.format("%-7s", product.getProductNumber());
                formatter.format("%-14.14s ", product.getName());
                formatter.format("(%3d) ", item.getQuantity());
                formatter.format("%s%7.2f", currencySymbol, product.getPrice() * item.getQuantity());
                formatter.format("\n");
                total += product.getPrice() * item.getQuantity();
            }

            formatter.format("----------------------------\n");
            formatter.format("Total                       ");
            formatter.format("%s%7.2f\n", currencySymbol, total);
            formatter.close();
        }

        return stringBuilder.toString();
    }

    /**
     * Gets the quantity of the specified item currently in stock
     *
     * @param productNumber Product number of product to check
     * @return Quantity in stock
     */
    public int getProductQuantity(String productNumber) {
        return productReader.getProductDetails(productNumber).getQuantity();
    }

    /**
     * Returns the image of a product
     *
     * @param item Item that the product is associated with
     * @return Image to display in a {@link clients.Picture}
     */
    public ImageIcon getItemIcon(Order.Item item) {
        return productReader.getProductImage(item.getProductNumber());
    }

    /**
     * Returns the name of a product
     *
     * @param item Item that the product is associated with
     * @return Name of product
     */
    public String getProductName(Order.Item item) {
        return productReader.getProductDetails(item.getProductNumber()).getName();
    }

    /**
     * make a Basket when required
     */
    private void makeBasketIfRequired() {
        if (currentOrder == null) {
            currentOrder = orderProcessor.createOrder();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Current state of the model
     */
    public enum State {
        PROCESS,
        CHECKED
    }

    /**
     * Properties that can be updated via {@link PropertyChangeSupport}
     */
    public final static class Property {
        public static final String PROMPT = "prompt";
        public static final String STATE = "currentState";
        public static final String ORDER_CONTENTS = "orderContents";
    }
}
  

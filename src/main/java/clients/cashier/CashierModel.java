package clients.cashier;

import clients.adapters.ProductNameAdapter;
import debug.DEBUG;
import logic.*;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;

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

    private boolean wasLastPromptError = false;

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

    public boolean getWasLastPromptError() {
        if (wasLastPromptError) {
            wasLastPromptError = false;
            return true;
        }

        return false;
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

        try {
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
        } catch (RemoteException e) {
            prompt = "Unable to connect to server";
            System.err.println("RemoteException: " + e.getMessage());
            wasLastPromptError = true;
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
            try {
                boolean stockBought = stockWriter.buyStock(currentProduct, quantity);

                if (stockBought) {
                    makeBasketIfRequired();
                    currentOrder.addItem(new Order.Item(currentProduct.getProductNumber(), quantity));
                    propertyChangeSupport.firePropertyChange(Property.ORDER_CONTENTS, null, currentOrder);
                    prompt = "Purchased " + currentProduct.getName();
                } else {
                    prompt = "!!! Not in stock";
                }

                // Return to State.PROCESS when done
                currentState = State.PROCESS;
                propertyChangeSupport.firePropertyChange(Property.STATE, null, currentState);
            } catch (RemoteException e) {
                prompt = "Unable to connect to server";
                System.err.println("RemoteException: " + e.getMessage());
                wasLastPromptError = true;
            }
        }

        propertyChangeSupport.firePropertyChange(Property.PROMPT, null, prompt);
    }

    /**
     * Customer pays for the contents of the basket
     */
    public void buyBasket() {
        String prompt = "";

        if (currentOrder != null && !currentOrder.isEmpty()) {
            try {
                orderProcessor.addOrderToQueue(currentOrder);

                prompt = "Start New Order";
                currentState = State.PROCESS;
                propertyChangeSupport.firePropertyChange(Property.STATE, null, currentState);

                currentOrder = null;
                propertyChangeSupport.firePropertyChange(Property.ORDER_CONTENTS, null, null);
            } catch (RemoteException e) {
                prompt = "Unable to connect to server";
                System.err.println("RemoteException: " + e.getMessage());
                wasLastPromptError = true;
            }
        }

        propertyChangeSupport.firePropertyChange(Property.PROMPT, null, prompt);
    }

    /**
     * ask for update of view called at start of day
     * or after system reset
     */
    public void askForUpdate() {
        propertyChangeSupport.firePropertyChange(Property.PROMPT, null, "Welcome!");
    }

    /**
     * Gets the quantity of the specified item currently in stock
     *
     * @param productNumber Product number of product to check
     * @return Quantity in stock
     *
     * @throws RuntimeException Shouldn't fail as the product information was previously available
     */
    public int getProductQuantity(String productNumber) {
        try {
            return productReader.getProductDetails(productNumber).getQuantity();
        } catch (RemoteException e) {
            System.err.println("RemoteException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the image of a product
     *
     * @param item Item that the product is associated with
     * @return Image to display in a {@link clients.Picture}
     *
     * @throws RuntimeException Shouldn't fail as the product information was previously available
     */
    public ImageIcon getItemIcon(Order.Item item) {
        try {
            return productReader.getProductImage(item.getProductNumber());
        } catch (RemoteException e) {
            System.err.println("RemoteException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the name of a product
     *
     * @param item Item that the product is associated with
     * @return Name of product
     *
     * @throws RuntimeException Shouldn't fail as the product information was previously available
     */
    public String getProductName(Order.Item item) {
        try {
            return productReader.getProductDetails(item.getProductNumber()).getName();
        } catch (RemoteException e) {
            System.err.println("RemoteException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * make a Basket when required
     */
    private void makeBasketIfRequired() throws RemoteException {
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
  

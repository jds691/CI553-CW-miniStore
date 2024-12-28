package clients.cashier;

import debug.DEBUG;
import logic.*;

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
        } catch (Exception e) {
            DEBUG.error("CashierModel.constructor\n%s", e.getMessage());
        }

        currentState = State.PROCESS;
    }

    /**
     * Get the Basket of products
     *
     * @return basket
     */
    public Order getCurrentOrder() {
        return currentOrder;
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

        if (productReader.doesProductExist(productNumber)) {
            Product product = productReader.getProductDetails(productNumber);
            if (product.getQuantity() >= amount) {
                prompt = String.format(
                        "%s : %7.2f (%2d) ",
                        product.getDescription(),
                        product.getPrice(),
                        product.getQuantity()
                );

                //   Remember prod.
                currentProduct = product;
                //   OK await BUY
                currentState = State.CHECKED;
            } else {
                prompt = product.getDescription() + " not in stock";
            }
        } else {
            prompt = "Unknown product number " + productNumber;
        }

        propertyChangeSupport.firePropertyChange(Property.PROMPT.toString(), null, prompt);
    }

    /**
     * Buy the product
     */
    public void buyCurrentProduct() {
        String prompt;

        if (currentState != State.CHECKED) {
            prompt = "please check its availability";
        } else {
            boolean stockBought = stockWriter.buyStock(currentProduct, 1);

            if (stockBought) {
                makeBasketIfRequired();
                currentOrder.addItem(new Order.Item(currentProduct.getProductNumber(), 1));
                prompt = "Purchased " + currentProduct.getDescription();
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
                formatter.format("%-14.14s ", product.getDescription());
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

    public enum State {
        PROCESS,
        CHECKED
    }

    public final static class Property {
        public static final String PROMPT = "prompt";
        public static final String STATE = "currentState";
    }
}
  

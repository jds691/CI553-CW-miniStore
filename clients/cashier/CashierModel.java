package clients.cashier;

import debug.DEBUG;
import logic.*;

import java.util.Observable;

/**
 * Implements the Model of the cashier client
 */
public class CashierModel extends Observable {
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
                //    & quantity
                currentProduct.setQuantity(amount);
                //   OK await BUY
                currentState = State.CHECKED;
            } else {
                prompt = product.getDescription() + " not in stock";
            }
        } else {
            prompt = "Unknown product number " + productNumber;
        }

        setChanged();
        notifyObservers(prompt);
    }

    /**
     * Buy the product
     */
    public void buyCurrentProduct() {
        String prompt;

        if (currentState != State.CHECKED) {
            prompt = "please check its availability";
        } else {
            boolean stockBought = stockWriter.buyStock(currentProduct.getProductNumber(), currentProduct.getQuantity());

            if (stockBought) {
                makeBasketIfRequired();
                currentOrder.addProduct(currentProduct);
                prompt = "Purchased " + currentProduct.getDescription();
            } else {
                prompt = "!!! Not in stock";
            }
        }

        // Return to State.PROCESS when done
        currentState = State.PROCESS;
        setChanged();
        notifyObservers(prompt);
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

        currentOrder = null;
        setChanged();
        notifyObservers(prompt);
    }

    /**
     * ask for update of view called at start of day
     * or after system reset
     */
    public void askForUpdate() {
        setChanged();
        notifyObservers("Welcome");
    }

    /**
     * make a Basket when required
     */
    private void makeBasketIfRequired() {
        if (currentOrder == null) {
            currentOrder = orderProcessor.createOrder();
        }
    }

    private enum State {
        PROCESS,
        CHECKED
    }
}
  

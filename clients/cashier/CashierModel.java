package clients.cashier;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.*;

import java.util.Observable;

/**
 * Implements the Model of the cashier client
 */
public class CashierModel extends Observable {
    /**
     * Current state of the model
     */
    private State currentState = State.PROCESS;
    // Current product
    private Product currentProduct = null;
    // Bought items
    private Basket basket = null;
    // Database access
    private StockReadWriter stockReadWriter = null;
    // Process order
    private OrderProcessor orderProcessor = null;

    /**
     * Construct the model of the Cashier
     *
     * @param factory The factory to create the connection objects
     */
    public CashierModel(MiddleFactory factory) {
        try {
            stockReadWriter = factory.makeStockReadWriter();
            orderProcessor = factory.makeOrderProcessor();
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
    public Basket getBasket() {
        return basket;
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

        try {
            if (stockReadWriter.doesProductExist(productNumber)) {
                Product product = stockReadWriter.getProductDetails(productNumber);
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
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doCheck", e.getMessage());
            prompt = e.getMessage();
        }

        setChanged();
        notifyObservers(prompt);
    }

    /**
     * Buy the product
     */
    public void buyCurrentProduct() {
        String prompt;

        try {
            if (currentState != State.CHECKED) {
                prompt = "please check its availability";
            } else {
                boolean stockBought = stockReadWriter.buyStock(currentProduct.getProductNumber(), currentProduct.getQuantity());

                if (stockBought) {
                    makeBasketIfRequired();
                    basket.add(currentProduct);
                    prompt = "Purchased " + currentProduct.getDescription();
                } else {
                    prompt = "!!! Not in stock";
                }
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doBuy", e.getMessage());
            prompt = e.getMessage();
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

        try {
            if (basket != null && !basket.isEmpty()) {
                orderProcessor.newOrder(basket);
            }

            prompt = "Start New Order";
            currentState = State.PROCESS;
        } catch (OrderException e) {
            DEBUG.error("%s\n%s", "CashierModel.doCancel", e.getMessage());
            prompt = e.getMessage();
        }

        basket = null;
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
        if (basket == null) {
            try {
                int uon = orderProcessor.uniqueNumber();
                //  basket list
                basket = makeBasket();
                // Add an order number
                basket.setOrderNumber(uon);
            } catch (OrderException e) {
                DEBUG.error("Comms failure\n" + "CashierModel.makeBasket()\n%s", e.getMessage());
            }
        }
    }

    /**
     * return an instance of a new Basket
     *
     * @return an instance of a new Basket
     */
    protected Basket makeBasket() {
        return new Basket();
    }

    private enum State {
        PROCESS,
        CHECKED
    }
}
  

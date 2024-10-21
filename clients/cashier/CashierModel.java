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
    // Current state
    private State theState = State.process;
    // Current product
    private Product theProduct = null;
    // Bought items
    private Basket theBasket = null;
    // Product being processed
    private String pn = "";
    // Database access
    private StockReadWriter theStock = null;
    // Process order
    private OrderProcessing theOrder = null;

    /**
     * Construct the model of the Cashier
     *
     * @param mf The factory to create the connection objects
     */
    public CashierModel(MiddleFactory mf) {
        try {
            theStock = mf.makeStockReadWriter();
            theOrder = mf.makeOrderProcessing();
        } catch (Exception e) {
            DEBUG.error("CashierModel.constructor\n%s", e.getMessage());
        }
        theState = State.process;
    }

    /**
     * Get the Basket of products
     *
     * @return basket
     */
    public Basket getBasket() {
        return theBasket;
    }

    /**
     * Check if the product is in Stock
     *
     * @param productNum The product number
     */
    public void doCheck(String productNum) {
        String theAction = "";
        theState = State.process;
        pn = productNum.trim();
        int amount = 1;
        try {
            if (theStock.exists(pn)) {
                Product pr = theStock.getDetails(pn);
                if (pr.getQuantity() >= amount) {
                    theAction = String.format(
                            "%s : %7.2f (%2d) ",
                            pr.getDescription(),
                            pr.getPrice(),
                            pr.getQuantity()
                    );

                    //   Remember prod.
                    theProduct = pr;
                    //    & quantity
                    theProduct.setQuantity(amount);
                    //   OK await BUY
                    theState = State.checked;
                } else {
                    theAction = pr.getDescription() + " not in stock";
                }
            } else {
                theAction = "Unknown product number " + pn;
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doCheck", e.getMessage());
            theAction = e.getMessage();
        }

        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Buy the product
     */
    public void doBuy() {
        String theAction = "";
        int amount = 1;
        try {
            if (theState != State.checked) {
                theAction = "please check its availablity";
            } else {
                boolean stockBought = theStock.buyStock(theProduct.getProductNum(), theProduct.getQuantity());
                if (stockBought) {
                    makeBasketIfReq();
                    theBasket.add(theProduct);
                    theAction = "Purchased " + theProduct.getDescription();
                } else {
                    theAction = "!!! Not in stock";
                }
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doBuy", e.getMessage());
            theAction = e.getMessage();
        }

        // Return to State.process when done
        theState = State.process;
        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Customer pays for the contents of the basket
     */
    public void doBought() {
        String theAction = "";
        try {
            if (theBasket != null && !theBasket.isEmpty()) {
                theOrder.newOrder(theBasket);
            }

            theAction = "Start New Order";
            theState = State.process;
        } catch (OrderException e) {
            DEBUG.error("%s\n%s", "CashierModel.doCancel", e.getMessage());
            theAction = e.getMessage();
        }

        theBasket = null;
        setChanged();
        notifyObservers(theAction); // Notify
    }

    /**
     * ask for update of view callled at start of day
     * or after system reset
     */
    public void askForUpdate() {
        setChanged();
        notifyObservers("Welcome");
    }

    /**
     * make a Basket when required
     */
    private void makeBasketIfReq() {
        if (theBasket == null) {
            try {
                int uon = theOrder.uniqueNumber();
                //  basket list
                theBasket = makeBasket();
                // Add an order number
                theBasket.setOrderNum(uon);
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
        process,
        checked
    }
}
  

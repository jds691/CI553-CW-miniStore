package clients.customer;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockException;
import middle.StockReader;

import javax.swing.*;
import java.util.Observable;

/**
 * Implements the Model of the customer client
 */
public class CustomerModel extends Observable {
    // Current product
    private final Product theProduct = null;
    private final OrderProcessing theOrder = null;
    // Bought items
    private Basket theBasket = null;
    // Product being processed
    private String pn = "";
    private StockReader theStock = null;
    private ImageIcon thePic = null;

    /**
     * Construct the model of the Customer
     *
     * @param mf The factory to create the connection objects
     */
    public CustomerModel(MiddleFactory mf) {
        try {
            // Database access
            theStock = mf.makeStockReader();
        } catch (Exception e) {
            DEBUG.error("CustomerModel.constructor\n" + "Database not created?\n%s\n", e.getMessage());
        }

        // Initial Basket
        theBasket = makeBasket();
    }

    /**
     * return the Basket of products
     *
     * @return the basket of products
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
        theBasket.clear();
        String theAction = "";
        // Product no.
        pn = productNum.trim();
        int amount = 1;
        try {
            if (theStock.exists(pn)) {
                Product pr = theStock.getDetails(pn);
                //  In stock?
                if (pr.getQuantity() >= amount)
                {
                    theAction = String.format(
                            "%s : %7.2f (%2d) ",
                            pr.getDescription(),
                            pr.getPrice(),
                            pr.getQuantity()
                    );
                    //   Require 1
                    pr.setQuantity(amount);
                    theBasket.add(pr);
                    thePic = theStock.getImage(pn);
                } else {
                    theAction = pr.getDescription() + " not in stock";
                }
            } else {
                theAction = "Unknown product number " + pn;
            }
        } catch (StockException e) {
            DEBUG.error("CustomerClient.doCheck()\n%s", e.getMessage());
        }

        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Clear the products from the basket
     */
    public void doClear() {
        String theAction = "";
        // Clear s. list
        theBasket.clear();
        theAction = "Enter Product Number";
        thePic = null;
        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Return a picture of the product
     *
     * @return An instance of an ImageIcon
     */
    public ImageIcon getPicture() {
        return thePic;
    }

    /**
     * ask for update of view callled at start
     */
    private void askForUpdate() {
        setChanged();
        notifyObservers("START only");
    }

    /**
     * Make a new Basket
     *
     * @return an instance of a new Basket
     */
    protected Basket makeBasket() {
        return new Basket();
    }
}


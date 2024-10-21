package clients.backDoor;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.StockException;
import middle.StockReadWriter;

import java.util.Observable;

/**
 * Implements the Model of the back door client
 */
public class BackDoorModel extends Observable {
    private Basket theBasket = null;            // Bought items
    private String pn = "";                      // Product being processed

    private StockReadWriter theStock = null;

    /**
     * Construct the model of the back door client
     * @param mf The factory to create the connection objects
     */
    public BackDoorModel(MiddleFactory mf) {
        try {
            theStock = mf.makeStockReadWriter();
        } catch (Exception e) {
            DEBUG.error("CustomerModel.constructor\n%s", e.getMessage());
        }

        theBasket = makeBasket();
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
     * Check The current stock level
     *
     * @param productNum The product number
     */
    public void doCheck(String productNum) {
        pn = productNum.trim();
    }

    /**
     * Query
     *
     * @param productNum The product number of the item
     */
    public void doQuery(String productNum) {
        String theAction = "";
        pn = productNum.trim();
        try {
            if (theStock.exists(pn)) {
                Product pr = theStock.getDetails(pn);
                theAction = String.format(
                        "%s : %7.2f (%2d) ",
                        pr.getDescription(),
                        pr.getPrice(),
                        pr.getQuantity()
                );
            } else {
                theAction = "Unknown product number " + pn;       //  product number
            }
        } catch (StockException e) {
            theAction = e.getMessage();
        }

        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Re stock
     *
     * @param productNum The product number of the item
     * @param quantity   How many to be added
     */
    public void doRStock(String productNum, String quantity) {
        String theAction = "";
        theBasket = makeBasket();
        pn = productNum.trim();
        String pn = productNum.trim();
        int amount = 0;
        try {
            String aQuantity = quantity.trim();
            try {
                amount = Integer.parseInt(aQuantity);

                if (amount < 0)
                    throw new NumberFormatException("-ve");

            } catch (Exception err) {
                theAction = "Invalid quantity";
                setChanged();
                notifyObservers(theAction);
                return;
            }

            if (theStock.exists(pn)) {
                theStock.addStock(pn, amount);
                Product pr = theStock.getDetails(pn);
                theBasket.add(pr);
                theAction = "";
            } else {
                theAction = "Unknown product number " + pn;
            }
        } catch (StockException e) {
            theAction = e.getMessage();
        }

        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Clear the product()
     */
    public void doClear() {
        String theAction = "";
        theBasket.clear();
        theAction = "Enter Product Number";
        setChanged();
        notifyObservers(theAction);
    }

    /**
     * return an instance of a Basket
     *
     * @return a new instance of a Basket
     */
    protected Basket makeBasket() {
        return new Basket();
    }
}


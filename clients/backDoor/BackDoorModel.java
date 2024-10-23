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
    /**
     * Bought items
     */
    private Basket basket = null;
    /**
     * Product being processed
     */
    private String productNumber = "";

    private StockReadWriter stockReadWriter = null;

    /**
     * Construct the model of the back door client
     * @param factory The factory to create the connection objects
     */
    public BackDoorModel(MiddleFactory factory) {
        try {
            stockReadWriter = factory.makeStockReadWriter();
        } catch (Exception e) {
            DEBUG.error("CustomerModel.constructor\n%s", e.getMessage());
        }

        basket = makeBasket();
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
        this.productNumber = productNumber.trim();

        try {
            if (stockReadWriter.doesProductExist(this.productNumber)) {
                Product product = stockReadWriter.getProductDetails(this.productNumber);
                prompt = String.format(
                        "%s : %7.2f (%2d) ",
                        product.getDescription(),
                        product.getPrice(),
                        product.getQuantity()
                );
            } else {
                prompt = "Unknown product number " + this.productNumber;
            }
        } catch (StockException e) {
            prompt = e.getMessage();
        }

        setChanged();
        notifyObservers(prompt);
    }

    /**
     * Restocks a product by the given quantity.
     *
     * @param productNumber The product number of the item
     * @param quantity   How many to be added
     */
    public void restockProduct(String productNumber, int quantity) {
        String prompt;
        basket = makeBasket();
        this.productNumber = productNumber.trim();

        try {
            if (stockReadWriter.doesProductExist(this.productNumber)) {
                stockReadWriter.addStock(this.productNumber, quantity);
                Product product = stockReadWriter.getProductDetails(this.productNumber);
                basket.add(product);
                prompt = "";
            } else {
                prompt = "Unknown product number " + this.productNumber;
            }
        } catch (StockException e) {
            prompt = e.getMessage();
        }

        setChanged();
        notifyObservers(prompt);
    }

    /**
     * Clear the product
     */
    //TODO: Figure out what this is actually responsible for
    public void reset() {
        String prompt = "Enter Product Number";
        basket.clear();
        setChanged();
        notifyObservers(prompt);
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


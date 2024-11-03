package clients.customer;

import models.Basket;
import logic.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.StockException;
import middle.StockReader;

import javax.swing.*;
import java.util.Observable;

/**
 * Implements the Model of the customer client
 */
public class CustomerModel extends Observable {
    // Bought items
    private Basket basket = null;
    private StockReader stockReader = null;
    private ImageIcon image = null;

    /**
     * Construct the model of the Customer
     *
     * @param factory The factory to create the connection objects
     */
    public CustomerModel(MiddleFactory factory) {
        try {
            // Database remote.access
            stockReader = factory.makeStockReader();
        } catch (Exception e) {
            DEBUG.error("CustomerModel.constructor\n" + "Database not created?\n%s\n", e.getMessage());
        }

        // Initial Basket
        basket = makeBasket();
    }

    /**
     * return the Basket of products
     *
     * @return the basket of products
     */
    public Basket getBasket() {
        return basket;
    }

    /**
     * Check if the product is in Stock
     *
     * @param productNumber The product number
     */
    public void queryProduct(String productNumber) {
        basket.clear();
        String prompt = "";
        // Product being processed
        productNumber = productNumber.trim();
        int amount = 1;
        try {
            if (stockReader.doesProductExist(productNumber)) {
                Product product = stockReader.getProductDetails(productNumber);
                //  In stock?
                if (product.getQuantity() >= amount) {
                    prompt = String.format(
                            "%s : %7.2f (%2d) ",
                            product.getDescription(),
                            product.getPrice(),
                            product.getQuantity()
                    );
                    //   Require 1
                    product.setQuantity(amount);
                    basket.add(product);
                    image = stockReader.getProductImage(productNumber);
                } else {
                    prompt = product.getDescription() + " not in stock";
                }
            } else {
                prompt = "Unknown product number " + productNumber;
            }
        } catch (StockException e) {
            DEBUG.error("CustomerClient.doCheck()\n%s", e.getMessage());
        }

        setChanged();
        notifyObservers(prompt);
    }

    /**
     * Clear the products from the basket
     */
    public void reset() {
        String prompt = "Enter Product Number";
        basket.clear();
        image = null;
        setChanged();
        notifyObservers(prompt);
    }

    /**
     * Return a picture of the product
     *
     * @return An instance of an ImageIcon
     */
    public ImageIcon getPicture() {
        return image;
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


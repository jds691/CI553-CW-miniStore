package clients.customer;

import debug.DEBUG;
import logic.LogicFactory;
import logic.Product;
import logic.ProductReader;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Implements the Model of the customer client
 */
public class CustomerModel extends Observable {
    // Bought items
    private ArrayList<Product> products = null;
    private ProductReader productReader = null;
    private ImageIcon image = null;

    /**
     * Construct the model of the Customer
     *
     * @param factory The factory to create the connection objects
     */
    public CustomerModel(LogicFactory factory) {
        try {
            // Database remote.access
            productReader = factory.getProductReader();
        } catch (Exception e) {
            DEBUG.error("CustomerModel.constructor\n" + "Database not created?\n%s\n", e.getMessage());
        }

        // Initial Basket
        products = new ArrayList<>();
    }

    /**
     * return the Basket of products
     *
     * @return the basket of products
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

    /**
     * Check if the product is in Stock
     *
     * @param productNumber The product number
     */
    public void queryProduct(String productNumber) {
        products.clear();
        String prompt = "";
        // Product being processed
        productNumber = productNumber.trim();
        int amount = 1;
        if (productReader.doesProductExist(productNumber)) {
            Product product = productReader.getProductDetails(productNumber);
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
                products.add(product);
                image = productReader.getProductImage(productNumber);
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
     * Clear the products from the basket
     */
    public void reset() {
        String prompt = "Enter Product Number";
        products.clear();
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
}


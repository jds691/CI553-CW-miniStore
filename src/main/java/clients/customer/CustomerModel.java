package clients.customer;

import debug.DEBUG;
import logic.LogicFactory;
import logic.Product;
import logic.ProductReader;

import javax.swing.*;
import java.util.Observable;

/**
 * Implements the Model of the customer client
 */
public class CustomerModel extends Observable {
    private ProductReader productReader = null;
    private ImageIcon image = null;
    private Product selectedProduct = null;

    /**
     * Construct the model of the Customer
     *
     * @param factory The factory to create the connection objects
     */
    public CustomerModel(LogicFactory factory) {
        try {
            productReader = factory.getProductReader();
        } catch (Exception e) {
            DEBUG.error("CustomerModel.constructor\n" + "Database not created?\n%s\n", e.getMessage());
        }
    }

    /**
     * Check if the product is in Stock
     *
     * @param productNumber The product number
     */
    public void queryProduct(String productNumber) {
        selectedProduct = null;

        String prompt = "";
        // Product being processed
        productNumber = productNumber.trim();
        if (productReader.doesProductExist(productNumber)) {
            selectedProduct = productReader.getProductDetails(productNumber);
            image = productReader.getProductImage(productNumber);
        } else {
            prompt = "Unknown product number " + productNumber;
        }

        setChanged();
        notifyObservers(prompt);
    }

    /**
     * Return a picture of the product
     *
     * @return An instance of an ImageIcon
     */
    public ImageIcon getProductImage() {
        return image;
    }

    public String getProductName() {
        return selectedProduct != null ? selectedProduct.getDescription() : null;
    }

    public String getProductMetadata() {
        return selectedProduct != null ?
                String.format("£%.2f • %d in stock", selectedProduct.getPrice(), selectedProduct.getQuantity()) :
                null;
    }

    public String getProductDescription() {
        return "Not implemented.";
    }
}


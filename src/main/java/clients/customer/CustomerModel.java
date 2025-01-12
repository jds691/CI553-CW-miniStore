package clients.customer;

import clients.adapters.ProductNameAdapter;
import logic.LogicFactory;
import logic.Product;
import logic.ProductReader;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;

/**
 * Implements the Model of the customer client
 */
public class CustomerModel {
    private ProductReader productReader = null;
    private ImageIcon image = null;
    private Product selectedProduct = null;
    private ProductNameAdapter productNameAdapter = null;

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Construct the model of the Customer
     *
     * @param factory The factory to create the connection objects
     */
    public CustomerModel(LogicFactory factory) {
        try {
            productReader = factory.getProductReader();
            productNameAdapter = new ProductNameAdapter(productReader);
        } catch (Exception e) {
            System.err.printf("Unable to create customer model\n" + "Database not created?\n%s\n", e.getMessage());
            throw new RuntimeException(e);
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

        // productNumber must be only numbers and at least 1
        if (!productNumber.matches("^[0-9]+$")) {
            productNumber = productNameAdapter.getProductNumber(productNumber);
        }

        try {
            if (productReader.doesProductExist(productNumber)) {
                selectedProduct = productReader.getProductDetails(productNumber);
                image = productReader.getProductImage(productNumber);
            } else {
                prompt = "Unknown product number " + productNumber;
            }
        } catch (RemoteException e) {
            System.err.println("RemoteException: " + e.getMessage());
            prompt = "Unable to connect to server";
        }

        propertyChangeSupport.firePropertyChange(Property.SELECTED_PRODUCT, null, selectedProduct);
        propertyChangeSupport.firePropertyChange(Property.PROMPT, null, prompt);
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
        return selectedProduct != null ? selectedProduct.getName() : null;
    }

    public String getProductMetadata() {
        return selectedProduct != null ?
                String.format("£%.2f • %d in stock", selectedProduct.getPrice(), selectedProduct.getQuantity()) :
                null;
    }

    public String getProductDescription() {
        return selectedProduct != null ? selectedProduct.getDescription() : null;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Properties that can be updated via {@link PropertyChangeSupport}
     */
    public final static class Property {
        public static final String PROMPT = "prompt";
        public static final String SELECTED_PRODUCT = "selectedProduct";
    }
}


package clients.backDoor;

import debug.DEBUG;
import logic.LogicFactory;
import logic.Product;
import logic.ProductReader;
import logic.StockWriter;

import java.util.Observable;

/**
 * Implements the Model of the back door client
 */
public class BackDoorModel extends Observable {
    /**
     * Product being processed
     */
    private String productNumber = "";

    private ProductReader productReader = null;
    private StockWriter stockWriter = null;

    private String history = "History:\n\n";

    /**
     * Construct the model of the back door client
     * @param factory The factory to create the connection objects
     */
    public BackDoorModel(LogicFactory factory) {
        try {
            productReader = factory.getProductReader();
            stockWriter = factory.getStockWriter();
        } catch (Exception e) {
            DEBUG.error("CustomerModel.constructor\n%s", e.getMessage());
        }
    }

    /**
     * Queries the product's description, price and quantity and outputs it to observers.
     *
     * @param productNumber The product number of the item
     */
    public void queryProduct(String productNumber) {
        String prompt;
        this.productNumber = productNumber.trim();

        if (productReader.doesProductExist(this.productNumber)) {
            Product product = productReader.getProductDetails(this.productNumber);
            prompt = String.format(
                    "%s : %7.2f (%2d) ",
                    product.getName(),
                    product.getPrice(),
                    product.getQuantity()
            );
        } else {
            prompt = "Unknown product number " + this.productNumber;
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
        this.productNumber = productNumber.trim();

        if (productReader.doesProductExist(this.productNumber)) {
            Product product = productReader.getProductDetails(this.productNumber);
            stockWriter.addStock(product, quantity);
            prompt = "";
            history += String.format("%s: (+%d) (Now: %d)\n", product.getName(), quantity, product.getQuantity());
        } else {
            prompt = "Unknown product number " + this.productNumber;
        }

        setChanged();
        notifyObservers(prompt);
    }

    /**
     * Clear the product
     */
    public void reset() {
        String prompt = "Enter Product Number";
        history = "History:\n\n";
        setChanged();
        notifyObservers(prompt);
    }
    
    public String getHistory() {
        return history;
    }
}


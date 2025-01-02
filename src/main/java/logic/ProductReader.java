package logic;

import remote.Repository;

import javax.swing.*;
import java.rmi.Remote;

/**
 * Used to retrieve product information from the PRODUCTTABLE.
 */
public interface ProductReader extends Remote {
    /**
     * Checks if the product exits in the stock list
     *
     * @param productNumber Product number
     * @return true if exists otherwise false
     */
    boolean doesProductExist(String productNumber);
    /**
     * Returns details about the product in the stock list
     *
     * @param pNum Product number
     * @return StockNumber, Description, Price, Quantity
     */
    Product getProductDetails(String pNum);
    /**
     * Returns an image of the product in the stock list
     *
     * @param pNum Product number
     * @return Image
     */
    ImageIcon getProductImage(String pNum);
    /**
     * Gets the underlying data repository this is reading from
     *
     * @return Data repository
     */
    Repository<Product> getRepository();
}

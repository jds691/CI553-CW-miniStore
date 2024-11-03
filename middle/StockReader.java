package middle;

import logic.Product;

import javax.swing.*;

/**
 * Interface for read remote.access to the stock list.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public interface StockReader {
    /**
     * Checks if the product exits in the stock list
     *
     * @param pNum Product number
     * @return true if exists otherwise false
     * @throws StockException if issue
     */
    boolean doesProductExist(String pNum) throws StockException;

    /**
     * Returns details about the product in the stock list
     *
     * @param pNum Product number
     * @return StockNumber, Description, Price, Quantity
     * @throws StockException if issue
     */
    Product getProductDetails(String pNum) throws StockException;


    /**
     * Returns an image of the product in the stock list
     *
     * @param pNum Product number
     * @return Image
     * @throws StockException if issue
     */
    ImageIcon getProductImage(String pNum) throws StockException;
}

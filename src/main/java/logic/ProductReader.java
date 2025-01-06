package logic;

import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

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
    boolean doesProductExist(String productNumber) throws RemoteException;
    /**
     * Returns details about the product in the stock list
     *
     * @param pNum Product number
     * @return StockNumber, Description, Price, Quantity
     */
    Product getProductDetails(String pNum) throws RemoteException;
    /**
     * Returns an image of the product in the stock list
     *
     * @param pNum Product number
     * @return Image
     */
    ImageIcon getProductImage(String pNum) throws RemoteException;
    /**
     * Gets all products from the remote repository
     *
     * @return All products
     */
    Product[] readAllProducts() throws RemoteException;
}

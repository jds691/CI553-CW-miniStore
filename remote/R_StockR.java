package remote;

import models.Product;
import remote.access.StockR;
import middle.StockException;

import javax.swing.*;
import java.io.Serial;
import java.rmi.RemoteException;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

/**
 * Implements Read remote.access to the stock list,
 * the stock list is held in a relational DataBase.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public class R_StockR
        extends java.rmi.server.UnicastRemoteObject
        implements RemoteStockR_I {

    @Serial
    private static final long serialVersionUID = 1;
    private final StockR stockReader;

    public R_StockR(String url) throws RemoteException, StockException {
        stockReader = new StockR();
    }

    /**
     * Checks if the product exits in the stock list
     *
     * @param pNum The product number
     * @return true if exists otherwise false
     */
    public synchronized boolean exists(String pNum) throws RemoteException, StockException {
        return stockReader.doesProductExist(pNum);
    }

    /**
     * Returns details about the product in the stock list
     *
     * @param pNum The product number
     * @return StockNumber, Description, Price, Quantity
     */
    public synchronized Product getDetails(String pNum) throws RemoteException, StockException {
        return stockReader.getProductDetails(pNum);
    }

    /**
     * Returns an image of the product
     * BUG: However this will not work for distributed version
     *     as an instance of an Image is not Serializable
     *
     * @param pNum The product number
     * @return Image
     */
    public synchronized ImageIcon getImage(String pNum) throws RemoteException, StockException {
        return stockReader.getProductImage(pNum);
    }
}

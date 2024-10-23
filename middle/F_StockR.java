package middle;

import catalogue.Product;
import debug.DEBUG;
import remote.RemoteStockR_I;

import javax.swing.*;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Facade for read access to the stock list.
 * The actual implementation of this is held on the middle tier.
 * The actual stock list is held in a relational DataBase on the
 * third tier.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public class F_StockR implements StockReader {
    private RemoteStockR_I stockReader = null;
    private final String stockURL;

    public F_StockR(String url) {
        DEBUG.trace("F_StockR: %s", url);
        stockURL = url;
    }

    private void connect() throws StockException {
        try {
            stockReader = (RemoteStockR_I) Naming.lookup(stockURL);
        } catch (Exception e) {
            stockReader = null;

            throw new StockException("Com: " + e.getMessage());
        }
    }

    /**
     * Checks if the product exits in the stock list
     *
     * @return true if exists otherwise false
     */
    public synchronized boolean doesProductExist(String number) throws StockException {
        DEBUG.trace("F_StockR:exists()");

        try {
            if (stockReader == null)
                connect();

            return stockReader.exists(number);
        } catch (RemoteException e) {
            stockReader = null;

            throw new StockException("Net: " + e.getMessage());
        }
    }

    /**
     * Returns details about the product in the stock list
     *
     * @return StockNumber, Description, Price, Quantity
     */
    public synchronized Product getProductDetails(String number) throws StockException {
        DEBUG.trace("F_StockR:getDetails()");

        try {
            if (stockReader == null)
                connect();

            return stockReader.getDetails(number);
        } catch (RemoteException e) {
            stockReader = null;

            throw new StockException("Net: " + e.getMessage());
        }
    }

    public synchronized ImageIcon getProductImage(String number) throws StockException {
        DEBUG.trace("F_StockR:getImage()");

        try {
            if (stockReader == null)
                connect();

            return stockReader.getImage(number);
        } catch (RemoteException e) {
            stockReader = null;

            throw new StockException("Net: " + e.getMessage());
        }
    }
}

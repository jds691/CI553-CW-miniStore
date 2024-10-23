package middle;

import models.Product;
import debug.DEBUG;
import remote.RemoteStockRW_I;

import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Facade for read/write remote.access to the stock list.
 * The actual implementation of this is held on the middle tier.
 * The actual stock list is held in a relational DataBase on the
 * third tier.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public class F_StockRW
        extends F_StockR
        implements StockReadWriter {

    private RemoteStockRW_I stockReadWriter = null;
    private final String stockURL;

    public F_StockRW(String url) {
        // Not used
        super(url);
        stockURL = url;
    }

    private void connect() throws StockException {
        try {
            stockReadWriter = (RemoteStockRW_I) Naming.lookup(stockURL);
        } catch (Exception e) {
            stockReadWriter = null;
            throw new StockException("Com: " + e.getMessage());
        }
    }

    /**
     * Buys stock and hence decrements number in stock list
     *
     * @return StockNumber, Description, Price, Quantity
     * @throws StockException if remote exception
     */
    public boolean buyStock(String number, int amount) throws StockException {
        DEBUG.trace("F_StockRW:buyStock()");

        try {
            if (stockReadWriter == null)
                connect();

            return stockReadWriter.buyStock(number, amount);
        } catch (RemoteException e) {
            stockReadWriter = null;

            throw new StockException("Net: " + e.getMessage());
        }
    }

    /**
     * Adds (Restocks) stock to the product list
     *
     * @param number Stock number
     * @param amount of stock
     * @throws StockException if remote exception
     */
    public void addStock(String number, int amount) throws StockException {
        DEBUG.trace("F_StockRW:addStock()");

        try {
            if (stockReadWriter == null)
                connect();

            stockReadWriter.addStock(number, amount);
        } catch (RemoteException e) {
            stockReadWriter = null;

            throw new StockException("Net: " + e.getMessage());
        }
    }

    /**
     * Modifies Stock details for a given product number.
     * Information modified: Description, Price
     *
     * @param detail Stock details to be modified
     * @throws StockException if remote exception
     */
    public void modifyStock(Product detail) throws StockException {
        DEBUG.trace("F_StockRW:modifyStock()");

        try {
            if (stockReadWriter == null)
                connect();

            stockReadWriter.modifyStock(detail);
        } catch (RemoteException e) {
            stockReadWriter = null;

            throw new StockException("Net: " + e.getMessage());
        }
    }
}

package logic;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Used for modifying the stock amounts for a product in STOCKTABLE.
 */
public interface StockWriter extends Remote {
    /**
     * Customer buys stock,
     * stock level is thus decremented by amount bought.
     *
     * @param product   Product
     * @param amount Quantity of product
     * @return StockNumber, Description, Price, Quantity
     */
    boolean buyStock(Product product, int amount) throws RemoteException;
    /**
     * Adds stock (Restocks) to store.
     *
     * @param product   Product number
     * @param amount Quantity of product
     */
    void addStock(Product product, int amount) throws RemoteException;
    /**
     * Modifies Stock details for a given product number.
     * Information modified: Description, Price
     *
     * @param detail Replace with this version of product
     */
    void modifyStock(Product detail) throws RemoteException;
}

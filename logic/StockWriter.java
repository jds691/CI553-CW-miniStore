package logic;

import java.rmi.Remote;

/**
 * Used for modifying the stock amounts for a product in STOCKTABLE.
 */
public interface StockWriter extends Remote {
    /**
     * Customer buys stock,
     * stock level is thus decremented by amount bought.
     *
     * @param productNumber   Product number
     * @param amount Quantity of product
     * @return StockNumber, Description, Price, Quantity
     */
    boolean buyStock(String productNumber, int amount);
    /**
     * Adds stock (Restocks) to store.
     *
     * @param productNumber   Product number
     * @param amount Quantity of product
     */
    void addStock(String productNumber, int amount);
    /**
     * Modifies Stock details for a given product number.
     * Information modified: Description, Price
     *
     * @param detail Replace with this version of product
     */
    void modifyStock(Product detail);
}

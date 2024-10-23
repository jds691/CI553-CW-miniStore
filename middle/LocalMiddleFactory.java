package middle;

import dbAccess.StockR;
import dbAccess.StockRW;
import orders.Order;

/**
 * Provide access to middle tier components.
 * Now only one instance of each middle tier object is created
 * @author Mike Smith University of Brighton
 * @version 2.1
 */
public class LocalMiddleFactory implements MiddleFactory {
    private static StockR stockReader = null;
    private static StockRW stockReadWriter = null;
    private static Order orderProcessor = null;

    /**
     * Return an object to access the database for read only access.
     * All users share this same object.
     */
    public StockReader makeStockReader() throws StockException {
        if (stockReader == null)
            stockReader = new StockR();

        return stockReader;
    }

    /**
     * Return an object to access the database for read/write access.
     * All users share this same object.
     */
    public StockReadWriter makeStockReadWriter() throws StockException {
        if (stockReadWriter == null)
            stockReadWriter = new StockRW();

        return stockReadWriter;
    }

    /**
     * Return an object to access the order processing system.
     * All users share this same object.
     */
    public OrderProcessor makeOrderProcessor() throws OrderException {
        if (orderProcessor == null)
            orderProcessor = new Order();

        return orderProcessor;
    }
}


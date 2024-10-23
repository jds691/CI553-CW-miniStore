/**
 * @author Mike Smith University of Brighton
 * Interface Middle factory
 * @version 2.0
 */

package middle;

/**
 * Provide remote.access to middle tier components.
 */
public interface MiddleFactory {
    /**
     * Return an object to remote.access the database for read only remote.access
     *
     * @return instance of StockReader
     * @throws StockException if issue
     */
    StockReader makeStockReader() throws StockException;

    /**
     * Return an object to remote.access the database for read/write remote.access
     *
     * @return instance of StockReadWriter
     * @throws StockException if issue
     */
    StockReadWriter makeStockReadWriter() throws StockException;

    /**
     * Return an object to remote.access the order processing system
     *
     * @return instance of OrderProcessing
     * @throws OrderException if issue
     */
    OrderProcessor makeOrderProcessor() throws OrderException;
}


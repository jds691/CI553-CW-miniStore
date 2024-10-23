package middle;

/**
 * Provide remote.access to middle tier components.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public class RemoteMiddleFactory implements MiddleFactory {
    private String theStockR_URL = "";
    private String theStockRW_URL = "";
    private String theOrder_URL = "";

    public void setStockRInfo(String url) {
        theStockR_URL = url;
    }

    public void setStockRWInfo(String url) {
        theStockRW_URL = url;
    }

    public void setOrderInfo(String url) {
        theOrder_URL = url;
    }

    /**
     * Return an object to remote.access the database for read only remote.access.
     * Access is via RMI
     */
    public StockReader makeStockReader() throws StockException {
        return new F_StockR(theStockR_URL);
    }

    /**
     * Return an object to remote.access the database for read/write remote.access.
     * Access is via RMI
     */
    public StockReadWriter makeStockReadWriter() throws StockException {
        return new F_StockRW(theStockRW_URL);
    }

    /**
     * Return an object to remote.access the order processing system.
     * Access is via RMI
     */
    public OrderProcessor makeOrderProcessor() throws OrderException {
        return new F_Order(theOrder_URL);
    }
}


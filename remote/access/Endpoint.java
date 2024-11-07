package remote.access;

/**
 * Location of the various objects accessed remotely.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public class Endpoint {
    public static String STOCK_WRITE = "rmi://localhost/stock_rw";
    public static String PRODUCT_READ = "rmi://localhost/stock_r";
    public static String ORDER = "rmi://localhost/order";
}

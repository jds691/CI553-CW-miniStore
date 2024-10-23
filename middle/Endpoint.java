package middle;

/**
 * Location of the various objects accessed remotely.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public class Endpoint {
    public static String STOCK_READ_WRITE = "rmi://localhost/stock_rw";
    public static String STOCK_READ_ONLY = "rmi://localhost/stock_r";
    public static String ORDER = "rmi://localhost/order";
}

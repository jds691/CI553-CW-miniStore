package middle;

import remote.R_Order;
import remote.R_StockR;
import remote.R_StockRW;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * The server for the middle tier.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
class Server {
    public static void main(String[] args) {
        String stockR = args.length < 1      // URL of stock R
                ? Names.STOCK_R       //  default  location
                : args[0];            //  supplied location

        String stockRW = args.length < 2     // URL of stock RW
                ? Names.STOCK_RW     //  default  location
                : args[1];           //  supplied location

        String order = args.length < 3    // URL of order manip
                ? Names.ORDER        //  default  location
                : args[2];           //  supplied location

        (new Server()).bind(stockR, stockRW, order);
    }

    private void bind(String urlStockR, String urlStockRW, String urlOrder) {
        R_StockR theStockR;
        R_StockRW theStockRW;
        R_Order theOrder;

        System.out.println("Server: ");
        try {
            LocateRegistry.createRegistry(1099);
            String IPAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Server IP address " + IPAddress);
        } catch (Exception e) {
            System.out.println("Fail Starting rmiregistry" + e.getMessage());
            System.exit(0);
        }

        try {
            theStockR = new R_StockR(urlStockR);
            Naming.rebind(urlStockR, theStockR);
            System.out.println("StockR  bound to: " + urlStockR);

            theStockRW = new R_StockRW(urlStockRW);
            Naming.rebind(urlStockRW, theStockRW);
            System.out.println("StockRW bound to: " + urlStockRW);

            theOrder = new R_Order(urlOrder);
            Naming.rebind(urlOrder, theOrder);
            System.out.println("Order   bound to: " + urlOrder);

        } catch (Exception err) {
            System.out.println("Fail Server: " + err.getMessage());
        }
    }
}

package server;

import logic.LocalLogicFactory;
import logic.OrderProcessor;
import logic.ProductReader;
import logic.StockWriter;
import remote.access.Endpoint;

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
    //TODO: Server has no compile errors, but fails to start due to a lack of RemoteExceptions on the method declarations
    public static void main(String[] args) {
        String stockR = args.length < 1      // URL of stock R
                ? Endpoint.PRODUCT_READ       //  default  location
                : args[0];            //  supplied location

        String stockRW = args.length < 2     // URL of stock RW
                ? Endpoint.STOCK_WRITE     //  default  location
                : args[1];           //  supplied location

        String order = args.length < 3    // URL of order manip
                ? Endpoint.ORDER        //  default  location
                : args[2];           //  supplied location

        (new Server()).bind(stockR, stockRW, order);
    }

    private void bind(String urlStockR, String urlStockRW, String urlOrder) {
        System.out.println("Server: ");

        try {
            LocateRegistry.createRegistry(1099);
            String IPAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Server IP address " + IPAddress);
        } catch (Exception e) {
            System.out.println("Fail Starting rmiregistry" + e.getMessage());
            System.exit(0);
        }

        LocalLogicFactory factory = new LocalLogicFactory();

        try {
            ProductReader productReaderWrapper = new ProductReaderRemoteWrapper(factory.getProductReader());
            Naming.rebind(urlStockR, productReaderWrapper);
            System.out.println("StockR  bound to: " + urlStockR);

            StockWriter stockWriterWrapper = new StockWriterRemoteWrapper(factory.getStockWriter());
            Naming.rebind(urlStockRW, stockWriterWrapper);
            System.out.println("StockRW bound to: " + urlStockRW);

            OrderProcessor orderProcessorWrapper = new OrderProcessorRemoteWrapper(factory.getOrderProcessor());
            Naming.rebind(urlOrder, orderProcessorWrapper);
            System.out.println("Order   bound to: " + urlOrder);

        } catch (Exception err) {
            System.out.println("Fail Server: " + err.getMessage());
        }
    }
}

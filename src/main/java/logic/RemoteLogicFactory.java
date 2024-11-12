package logic;

import java.rmi.Naming;

/**
 * Provides data access via calls to Java's RMI system.
 */
public class RemoteLogicFactory implements LogicFactory {
    private OrderProcessor orderProcessor;
    private ProductReader productReader;
    private StockWriter stockWriter;

    public RemoteLogicFactory(String ordersEndpoint, String productsEndpoint, String stockEndpoint) {
        try {
            orderProcessor = (OrderProcessor) Naming.lookup(ordersEndpoint);
        } catch (Exception e) {
            orderProcessor = null;

            System.err.println("Unable to access an OrderProcessor instance via RMI, expect errors");
            e.printStackTrace();
        }

        try {
            productReader = (ProductReader) Naming.lookup(productsEndpoint);
        } catch (Exception e) {
            productReader = null;

            System.err.println("Unable to access an ProductReader instance via RMI, expect errors");
            e.printStackTrace();
        }

        try {
            stockWriter = (StockWriter) Naming.lookup(stockEndpoint);
        } catch (Exception e) {
            stockWriter = null;

            System.err.println("Unable to access an StockWriter instance via RMI, expect errors");
            e.printStackTrace();
        }
    }

    @Override
    public OrderProcessor getOrderProcessor() {
        return orderProcessor;
    }

    @Override
    public ProductReader getProductReader() {
        return productReader;
    }

    @Override
    public StockWriter getStockWriter() {
        return stockWriter;
    }
}

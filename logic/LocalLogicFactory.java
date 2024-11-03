package logic;

/**
 * Provides data access via calls to the local VM
 */
public class LocalLogicFactory implements LogicFactory {
    private static OrderProcessor orderProcessor;
    private static ProductReader productReader;
    private static StockWriter stockWriter;

    @Override
    public OrderProcessor getOrderProcessor() {
        if (orderProcessor == null) {
            orderProcessor = new OrderProcessorImpl();
        }

        return orderProcessor;
    }

    @Override
    public ProductReader getProductReader() {
        if (productReader == null) {
            productReader = new ProductReaderImpl();
        }

        return productReader;
    }

    @Override
    public StockWriter getStockWriter() {
        if (stockWriter == null) {
            stockWriter = new StockWriterImpl();
        }

        return stockWriter;
    }
}

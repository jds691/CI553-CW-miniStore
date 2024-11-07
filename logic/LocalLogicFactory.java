package logic;

import remote.RepositoryFactory;
import remote.SQLRepositoryFactory;
import remote.access.DBAccessFactory;

/**
 * Provides data access via calls to the local VM
 */
public class LocalLogicFactory implements LogicFactory {
    private static OrderProcessor orderProcessor;
    private static ProductReader productReader;
    private static StockWriter stockWriter;

    private static RepositoryFactory repositoryFactory;

    public LocalLogicFactory() {
        repositoryFactory = new SQLRepositoryFactory((new DBAccessFactory()).getNewDBAccess());
    }

    @Override
    public OrderProcessor getOrderProcessor() {
        if (orderProcessor == null) {
            orderProcessor = new OrderProcessorImpl(repositoryFactory.getOrderRepository());
        }

        return orderProcessor;
    }

    @Override
    public ProductReader getProductReader() {
        if (productReader == null) {
            productReader = new ProductReaderImpl(repositoryFactory.getProductRepository());
        }

        return productReader;
    }

    @Override
    public StockWriter getStockWriter() {
        if (stockWriter == null) {
            stockWriter = new StockWriterImpl(repositoryFactory.getStockRepository());
        }

        return stockWriter;
    }
}

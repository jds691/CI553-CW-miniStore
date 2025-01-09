package logic;

import remote.RepositoryFactory;

/**
 * Provides data access via calls to the local VM
 */
public class LocalLogicFactory implements LogicFactory {
    private OrderProcessor orderProcessor;
    private ProductReader productReader;
    private StockWriter stockWriter;

    private final RepositoryFactory repositoryFactory;

    public LocalLogicFactory(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
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

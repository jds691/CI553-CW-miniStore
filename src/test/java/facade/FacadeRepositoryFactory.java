package facade;

import logic.Order;
import logic.Product;
import remote.Repository;
import remote.RepositoryFactory;

public class FacadeRepositoryFactory implements RepositoryFactory {
    private Repository<Order> orderRepository;
    private Repository<Product> productRepository;
    private Repository<Product> stockRepository;

    public FacadeRepositoryFactory() {
        orderRepository = new FacadeOrderRepository();
        productRepository = new FacadeProductRepository();
        stockRepository = new FacadeProductRepository();
    }

    @Override
    public Repository<Order> getOrderRepository() {
        return orderRepository;
    }

    @Override
    public Repository<Product> getProductRepository() {
        return productRepository;
    }

    @Override
    public Repository<Product> getStockRepository() {
        return stockRepository;
    }

    public void setOrderRepository(Repository<Order> orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void setProductRepository(Repository<Product> productRepository) {
        this.productRepository = productRepository;
    }

    public void setStockRepository(Repository<Product> stockRepository) {
        this.stockRepository = stockRepository;
    }
}

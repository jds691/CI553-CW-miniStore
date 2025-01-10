package facade;

import logic.Order;
import logic.Product;
import remote.Repository;
import remote.RepositoryFactory;

public class FacadeRepositoryFactory implements RepositoryFactory {
    @Override
    public Repository<Order> getOrderRepository() {
        return new FacadeOrderRepository();
    }

    @Override
    public Repository<Product> getProductRepository() {
        return new FacadeProductRepository();
    }

    @Override
    public Repository<Product> getStockRepository() {
        return new FacadeProductRepository();
    }
}

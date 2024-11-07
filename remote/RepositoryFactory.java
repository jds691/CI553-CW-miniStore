package remote;

import logic.Order;
import logic.Product;

public interface RepositoryFactory {
    Repository<Order> getOrderRepository();
    Repository<Product> getProductRepository();
    Repository<Product> getStockRepository();
}

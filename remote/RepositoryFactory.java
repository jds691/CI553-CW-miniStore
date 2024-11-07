package remote;

import logic.Product;

public interface RepositoryFactory {
    Repository<Product> getProductRepository();
    Repository<Product> getStockRepository();
}

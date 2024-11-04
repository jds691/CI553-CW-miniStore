package remote;

import logic.Product;

public interface RepositoryFactory {
    Repository<Product, String> getProductRepository();
    Repository<Product, String> getStockRepository();
}

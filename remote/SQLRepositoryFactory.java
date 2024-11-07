package remote;

import logic.Product;
import remote.access.DBAccess;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLRepositoryFactory implements RepositoryFactory {
    private static Connection connection;

    private static Repository<Product> productRepository;
    private static Repository<Product> stockRepository;

    public SQLRepositoryFactory(DBAccess dbAccess) {
        try {
            dbAccess.loadDriver();

            connection = DriverManager.getConnection(
                    dbAccess.getUrlOfDatabase(),
                    dbAccess.getUsername(),
                    dbAccess.getPassword()
            );

            connection.setAutoCommit(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Repository<Product> getProductRepository() {
        if (productRepository == null) {
            productRepository = new ProductRepository(connection);
        }

        return productRepository;
    }

    public Repository<Product> getStockRepository() {
        if (stockRepository == null) {
            stockRepository = new StockRepository(connection);
        }

        return stockRepository;
    }
}

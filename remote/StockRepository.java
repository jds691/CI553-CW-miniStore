package remote;

import logic.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class StockRepository extends Repository<Product, String> {
    public StockRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Product create() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Product read(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean update(Product product) {
        try {
            try (PreparedStatement statement = connection.prepareStatement(
                    "update ProductTable set description = ?, price = ? where productNo = ?"
            )) {
                statement.setString(1, product.getDescription());
                statement.setDouble(2, product.getPrice());
                statement.setString(3, product.getProductNumber());

                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "update StockTable set stockLevel = ? where productNo = ?"
            )) {
                statement.setInt(1, product.getQuantity());
                statement.setString(2, product.getProductNumber());

                statement.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Product product) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

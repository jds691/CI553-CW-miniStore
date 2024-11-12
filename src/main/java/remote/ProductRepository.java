package remote;

import logic.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class ProductRepository extends Repository<Product> {
    public ProductRepository(Connection connection) {
        super(connection);
    }

    @Override
    public synchronized Product create() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized Product read(String id) {
        try {
            ProductImpl product = new ProductImpl(
                    "0",
                    "",
                    0.00,
                    0,
                    "default.jpg"
            );

            ResultSet results;
            try (PreparedStatement statement = connection.prepareStatement(
                    "select description, price, stockLevel, picture from ProductTable, StockTable where  ProductTable.productNo = ? and StockTable.productNo = ?"
            )) {
                statement.setString(1, id);
                statement.setString(2, id);

                results = statement.executeQuery();

                if (results.next()) {
                    product.setProductNumber(id);
                    product.setDescription(results.getString("description"));
                    product.setPrice(results.getDouble("price"));
                    product.setQuantity(results.getInt("stockLevel"));
                    product.setImageFilename(results.getString("picture"));
                }

                results.close();

                return product;
            }
        } catch (SQLException e) {

        }

        return null;
    }

    @Override
    public Product[] readAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized boolean update(Product entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized void delete(Product entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

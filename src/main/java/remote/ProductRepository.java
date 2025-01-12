package remote;

import logic.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class ProductRepository extends Repository<Product> {
    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
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
                    "",
                    0.00,
                    0,
                    "default.jpg"
            );

            ResultSet results;
            try (PreparedStatement statement = connection.prepareStatement(
                    "select name, description, price, stockLevel, picture from ProductTable, StockTable where  ProductTable.productNo = ? and StockTable.productNo = ?"
            )) {
                statement.setString(1, id);
                statement.setString(2, id);

                results = statement.executeQuery();

                if (results.next()) {
                    product.setProductNumber(id);
                    product.setName(results.getString("name"));
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
        ArrayList<Product> products = new ArrayList<>();

        try {
            ResultSet results;
            try (PreparedStatement statement = connection.prepareStatement(
                    "select PRODUCTNO from ProductTable"
            )) {
                results = statement.executeQuery();

                if (results.next()) {
                    products.add(read(results.getString("PRODUCTNO")));
                }

                results.close();
            }
        } catch (SQLException e) {

        }

        return products.toArray(new Product[0]);
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

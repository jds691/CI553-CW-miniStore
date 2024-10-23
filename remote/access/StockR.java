package remote.access;

import models.Product;
import debug.DEBUG;
import middle.StockException;
import middle.StockReader;

import javax.swing.*;
import java.sql.*;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

// mySQL
//    no spaces after SQL statement ;

/**
 * Implements Read remote.access to the stock list
 * The stock list is held in a relational DataBase
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public class StockR implements StockReader {
    private final Connection connection;

    /**
     * Connects to database
     * Uses a factory method to help set up the connection
     *
     * @throws StockException if problem
     */
    public StockR() throws StockException {
        try {
            DBAccess dbDriver = (new DBAccessFactory()).getNewDBAccess();
            dbDriver.loadDriver();

            connection = DriverManager.getConnection(
                    dbDriver.getUrlOfDatabase(),
                    dbDriver.getUsername(),
                    dbDriver.getPassword()
            );

            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new StockException("SQL problem:" + e.getMessage());
        } catch (Exception e) {
            throw new StockException("Can not load database driver.");
        }
    }

    /**
     * Returns a connection object that is used to process
     * requests to the DataBase
     *
     * @return a connection object
     */
    protected Connection getConnection() {
        return connection;
    }

    /**
     * Checks if the product exits in the stock list
     *
     * @param pNum The product number
     * @return true if exists otherwise false
     */
    public synchronized boolean doesProductExist(String pNum) throws StockException {
        try {
            ResultSet rs;
            try (PreparedStatement statement = connection.prepareStatement(
                    "select price from ProductTable where ProductTable.productNo = ?"
            )) {
                statement.setString(1, pNum);

                rs = statement.executeQuery();
            }

            boolean res = rs.next();
            DEBUG.trace("DB StockR: exists(%s) -> %s", pNum, (res ? "T" : "F"));

            return res;
        } catch (SQLException e) {
            throw new StockException("SQL exists: " + e.getMessage());
        }
    }

    /**
     * Returns details about the product in the stock list.
     * Assumed to exist in database.
     *
     * @param pNum The product number
     * @return Details in an instance of a Product
     */
    public synchronized Product getProductDetails(String pNum) throws StockException {
        try {
            Product product = new Product("0", "", 0.00, 0);

            ResultSet results;
            try (PreparedStatement statement = connection.prepareStatement(
                    "select description, price, stockLevel from ProductTable, StockTable where  ProductTable.productNo = ? and StockTable.productNo = ?"
            )) {
                statement.setString(1, pNum);
                statement.setString(2, pNum);

                results = statement.executeQuery();
            }

            if (results.next()) {
                product.setProductNumber(pNum);
                product.setDescription(results.getString("description"));
                product.setPrice(results.getDouble("price"));
                product.setQuantity(results.getInt("stockLevel"));
            }

            results.close();

            return product;
        } catch (SQLException e) {
            throw new StockException("SQL getDetails: " + e.getMessage());
        }
    }

    /**
     * Returns 'image' of the product
     *
     * @param pNum The product number
     *             Assumed to exist in database.
     * @return ImageIcon representing the image
     */
    public synchronized ImageIcon getProductImage(String pNum) throws StockException {
        String filename = "default.jpg";
        try {
            ResultSet results;
            try (PreparedStatement statement = connection.prepareStatement(
                    "select picture from ProductTable where ProductTable.productNo = ?"
            )) {
                statement.setString(1, pNum);
                results = statement.executeQuery();
            }

            boolean res = results.next();
            if (res)
                filename = results.getString("picture");

            results.close();
        } catch (SQLException e) {
            DEBUG.error("getImage()\n%s\n", e.getMessage());
            throw new StockException("SQL getImage: " + e.getMessage());
        }

        return new ImageIcon(filename);
    }
}

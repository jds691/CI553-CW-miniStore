package remote.access;

import models.Product;
import debug.DEBUG;
import middle.StockException;
import middle.StockReadWriter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods
// 

/**
 * Implements Read /Write remote.access to the stock list
 * The stock list is held in a relational DataBase
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public class StockRW extends StockR implements StockReadWriter {
    public StockRW() throws StockException {
        super();
    }

    /**
     * Customer buys stock, quantity decreased if sucessful.
     *
     * @param pNum   Product number
     * @param amount Amount of stock bought
     * @return true if succeeds else false
     */
    public synchronized boolean buyStock(String pNum, int amount) throws StockException {
        DEBUG.trace("DB StockRW: buyStock(%s,%d)", pNum, amount);
        int updates;
        try {
            try (PreparedStatement statement = getConnection().prepareStatement(
                    "update StockTable set stockLevel = stockLevel - ? where productNo = ? and stockLevel >= ?"
            )) {
                statement.setInt(1, amount);
                statement.setString(2, pNum);
                statement.setInt(3, amount);

                updates = statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new StockException("SQL buyStock: " + e.getMessage());
        }

        DEBUG.trace("buyStock() updates -> %n", updates);
        return updates > 0;   // success ?
    }

    /**
     * Adds stock (Re-stocks) to the store.
     * Assumed to exist in database.
     *
     * @param pNum   Product number
     * @param amount Amount of stock to add
     */
    public synchronized void addStock(String pNum, int amount) throws StockException {
        try {
            try (PreparedStatement statement = getConnection().prepareStatement(
                    "update StockTable set stockLevel = stockLevel + ? where productNo = ?"
            )) {
                statement.setInt(1, amount);
                statement.setString(2, pNum);

                statement.executeUpdate();
            }

            DEBUG.trace("DB StockRW: addStock(%s,%d)", pNum, amount);
        } catch (SQLException e) {
            throw new StockException("SQL addStock: " + e.getMessage());
        }
    }

    /**
     * Modifies Stock details for a given product number.
     * Assumed to exist in database.
     * Information modified: Description, Price
     *
     * @param detail Product details to change stocklist to
     */
    public synchronized void modifyStock(Product detail) throws StockException {
        DEBUG.trace("DB StockRW: modifyStock(%s)",
                detail.getProductNumber());
        try {
            if (!doesProductExist(detail.getProductNumber())) {
                try (PreparedStatement statement = getConnection().prepareStatement(
                        "insert into ProductTable values (?, ?, ?, ?)"
                )) {
                    statement.setString(1, detail.getProductNumber());
                    statement.setString(2, detail.getDescription());
                    statement.setString(3, String.format("images/Pic%s.jpg", detail.getProductNumber()));
                    statement.setDouble(4, detail.getPrice());
                    statement.executeUpdate();
                }

                try (PreparedStatement statement = getConnection().prepareStatement(
                        "insert into StockTable values (?, ?)"
                )) {
                    statement.setString(1, detail.getProductNumber());
                    statement.setInt(2, detail.getQuantity());
                    statement.executeUpdate();
                }
            } else {
                try (PreparedStatement statement = getConnection().prepareStatement(
                        "update ProductTable set description = ?, price = ? where productNo = ?"
                )) {
                    statement.setString(1, detail.getDescription());
                    statement.setDouble(2, detail.getPrice());
                    statement.setString(3, detail.getProductNumber());

                    statement.executeUpdate();
                }

                try (PreparedStatement statement = getConnection().prepareStatement(
                        "update StockTable set stockLevel = ? where productNo = ?"
                )) {
                    statement.setInt(1, detail.getQuantity());
                    statement.setString(2, detail.getProductNumber());

                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new StockException("SQL modifyStock: " + e.getMessage());
        }
    }
}

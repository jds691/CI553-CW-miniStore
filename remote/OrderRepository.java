package remote;

import logic.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class OrderRepository extends Repository<Order> {
    public OrderRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Order create() {
        // The clients create replicas of this quite regularly
        // This is to reduce polluting the table with empty orders, taking up IDs
        // The order will actually be created when update() is called on it
        return new OrderImpl();
    }

    @Override
    public Order read(String id) {
        Order order = new OrderImpl();

        try {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM ORDERTABLE WHERE ORDERID = ?")) {
                statement.setInt(1, Integer.parseInt(id));

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        order.setOrderNumber(resultSet.getInt(1));
                        order.setState(Order.State.values()[resultSet.getInt(2)]);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        try {
            try (PreparedStatement statement = connection.prepareStatement("SELECT PRODUCTNO, QUANTITY FROM ORDERPRODUCTTABLE WHERE ORDERID = ?")) {
                statement.setInt(1, Integer.parseInt(id));

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Order.Item item = new Order.Item(resultSet.getString(1), resultSet.getInt(2));

                        order.addItem(item);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return order;
    }

    @Override
    public boolean update(Order order) {
        if (order.getOrderNumber() == 0) {
            // Order has not been stored to database yet
            try {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO ORDERTABLE (STATE) VALUES (?)")) {
                    statement.setInt(1, Order.State.WAITING.ordinal());
                    statement.executeUpdate();
                }

                try (PreparedStatement statement = connection.prepareStatement("SELECT MAX(ORDERID) FROM ORDERTABLE")) {
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            order.setOrderNumber(resultSet.getInt(1));
                        } else {
                            return false;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            return insertProductsForOrder(order);
        } else {
            try {
                try (PreparedStatement statement = connection.prepareStatement("UPDATE ORDERTABLE SET STATE = ? WHERE ORDERID = ?")) {
                    statement.setInt(1, order.getOrderNumber());
                    statement.setInt(2, order.getState().ordinal());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            try {
                // Just erase the whole table as needed and rebuild it
                try (PreparedStatement statement = connection.prepareStatement("DELETE FROM ORDERPRODUCTTABLE WHERE ORDERID = ?")) {
                    statement.setInt(1, order.getOrderNumber());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            return insertProductsForOrder(order);
        }
    }

    @Override
    public void delete(Order order) {
        try {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM ORDERPRODUCTTABLE WHERE ORDERID = ?")) {
                statement.setInt(1, order.getOrderNumber());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM ORDERTABLE WHERE ORDERID = ?")) {
                statement.setInt(1, order.getOrderNumber());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    private boolean insertProductsForOrder(Order order) {
        try {
            String valueInsertions = "";
            for (int i = 0; i < order.getAllItems().length; i++) {
                valueInsertions += " (?, ?, ?)";
            }

            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO ORDERPRODUCTTABLE VALUES" + valueInsertions)) {
                int parameterIndex = 1;
                for (Order.Item item : order.getAllItems()) {
                    statement.setInt(parameterIndex, order.getOrderNumber());
                    statement.setString(parameterIndex + 1, item.getProductNumber());
                    statement.setInt(parameterIndex + 2, item.getQuantity());

                    parameterIndex += 3;
                }
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}

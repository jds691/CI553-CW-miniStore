package remote;

import logic.Order;

import java.sql.Connection;

class OrderRepository extends Repository<Order> {
    public OrderRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Order create() {
        return null;
    }

    @Override
    public Order read(String id) {
        return null;
    }

    @Override
    public boolean update(Order order) {
        return false;
    }

    @Override
    public void delete(Order order) {

    }
}

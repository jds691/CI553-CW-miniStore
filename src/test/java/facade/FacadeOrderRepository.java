package facade;

import logic.Order;

public class FacadeOrderRepository extends FacadeRepository<Order> {
    private int orderNumber = 0;

    @Override
    public Order create() {
        return new FacadeOrder();
    }

    @Override
    public Order read(String id) {
        return (Order) entities.stream().filter(x -> x.getOrderNumber() == Integer.parseInt(id)).toArray()[0];
    }

    @Override
    public Order[] readAll() {
        return new Order[0];
    }

    @Override
    public boolean update(Order order) {
        if (order.getOrderNumber() == 0) {
            order.setOrderNumber(++orderNumber);
            entities.add(order);
        } else {
            if (!contains(order)) {
                return false;
            }

            Order og = (Order) entities.stream().filter(x -> x.getOrderNumber() == order.getOrderNumber()).toArray()[0];
            int index = entities.indexOf(og);

            og.removeAllItems();
            for (Order.Item item : order.getAllItems()) {
                og.addItem(item);
            }
            og.setState(order.getState());
            entities.set(index, og);
        }

        return true;
    }

    @Override
    public void delete(Order order) {
        entities.remove(order);
    }
}

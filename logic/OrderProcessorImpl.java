package logic;

public class OrderProcessorImpl implements OrderProcessor {
    //TODO: When creating the processor, find the last order number
    private int uniqueNumber = 0;

    @Override
    public Order createOrder() {
        Order order = new OrderImpl();
        order.setOrderNumber(uniqueNumber++);

        return new OrderImpl();
    }

    @Override
    public void addOrderToQueue(Order order) {

    }

    @Override
    public Order popOrder() {
        return null;
    }

    @Override
    public void setOrderState(Order order, State state) {

    }
}

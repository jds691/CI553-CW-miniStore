package logic;

import java.util.ArrayDeque;

class OrderProcessorImpl implements OrderProcessor {
    //TODO: When creating the processor, find the last order number
    private int uniqueNumber = 0;

    private ArrayDeque<Order>[] currentOrders;

    OrderProcessorImpl() {
        currentOrders = new ArrayDeque[State.values().length];

        for (int i = 0; i < State.values().length; i++) {
            currentOrders[i] = new ArrayDeque<>();
        }
    }

    @Override
    public Order createOrder() {
        Order order = new OrderImpl();
        order.setOrderNumber(uniqueNumber++);

        return new OrderImpl();
    }

    @Override
    public void addOrderToQueue(Order order) {
        currentOrders[State.WAITING.ordinal()].add(order);
    }

    @Override
    public Order popOrder() {
        return currentOrders[State.WAITING.ordinal()].pop();
    }

    @Override
    public void setOrderState(Order order, State state) {
        currentOrders[state.ordinal()].add(order);
    }

    @Override
    public Order[] getAllOrdersInState(State state) {
        return currentOrders[state.ordinal()].toArray(new Order[0]);
    }
}

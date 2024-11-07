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
    public synchronized Order createOrder() {
        Order order = new OrderImpl();
        order.setOrderNumber(uniqueNumber++);

        return new OrderImpl();
    }

    @Override
    public synchronized void addOrderToQueue(Order order) {
        currentOrders[State.WAITING.ordinal()].add(order);
    }

    @Override
    public synchronized Order popOrder() {
        if (currentOrders[State.WAITING.ordinal()].isEmpty()) {
            return null;
        } else {
            return currentOrders[State.WAITING.ordinal()].pop();
        }
    }

    @Override
    public synchronized Order popOrder(State state) {
        if (currentOrders[state.ordinal()].isEmpty()) {
            return null;
        } else {
            return currentOrders[state.ordinal()].pop();
        }
    }

    @Override
    public synchronized void setOrderState(Order order, State state) {
        currentOrders[state.ordinal()].add(order);
    }

    @Override
    public synchronized Order[] getAllOrdersInState(State state) {
        return currentOrders[state.ordinal()].toArray(new Order[0]);
    }
}

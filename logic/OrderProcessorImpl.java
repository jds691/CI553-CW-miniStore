package logic;

import remote.Repository;

import java.util.ArrayDeque;

import static logic.Order.State;

class OrderProcessorImpl implements OrderProcessor {
    private Repository<Order> orderRepository;

    //TODO: Find a way to sync this with the repository on a regular basis
    private ArrayDeque<Order>[] currentOrders;

    OrderProcessorImpl(Repository<Order> orderRepository) {
        this.orderRepository = orderRepository;
        currentOrders = new ArrayDeque[State.values().length];

        for (int i = 0; i < State.values().length; i++) {
            currentOrders[i] = new ArrayDeque<>();
        }
    }

    @Override
    public synchronized Order createOrder() {
        return orderRepository.create();
    }

    @Override
    public synchronized void addOrderToQueue(Order order) {
        currentOrders[order.getState().ordinal()].add(order);
    }

    @Override
    public synchronized Order popOrder() {
        return popOrder(State.WAITING);
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
    public synchronized Order[] getAllOrdersInState(State state) {
        return currentOrders[state.ordinal()].toArray(new Order[0]);
    }
}

package logic;

import remote.Repository;

import java.util.ArrayDeque;
import java.util.Arrays;

import static logic.Order.State;

class OrderProcessorImpl implements OrderProcessor {
    private Repository<Order> orderRepository;

    //TODO: Find a way to sync this with the repository on a regular basis
    private ArrayDeque<Order>[] currentOrders;

    OrderProcessorImpl(Repository<Order> orderRepository) {
        this.orderRepository = orderRepository;

        currentOrders = createCurrentOrdersDeque();
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

    //REVIEW: Oh my god this seems woefully inefficient
    @Override
    public boolean requestDataRefresh() {
        ArrayDeque<Order>[] newCurrentOrders = createCurrentOrdersDeque();

        boolean didRefresh = currentOrders == newCurrentOrders;

        currentOrders = newCurrentOrders;

        return didRefresh;
    }

    private ArrayDeque<Order>[] createCurrentOrdersDeque() {
        Order[] orders = orderRepository.readAll();
        ArrayDeque<Order>[] currentOrders = new ArrayDeque[State.values().length];

        for (int i = 0; i < State.values().length; i++) {
            currentOrders[i] = new ArrayDeque<>();
            int finalI = i;
            Arrays.stream(orders)
                    .filter(order -> order.getState().ordinal() == finalI)
                    .forEach(currentOrders[i]::add);
        }

        return currentOrders;
    }
}

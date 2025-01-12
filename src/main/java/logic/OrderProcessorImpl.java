package logic;

import remote.Repository;

import java.util.ArrayDeque;
import java.util.Arrays;

import static logic.Order.State;

class OrderProcessorImpl implements OrderProcessor {
    private boolean didManuallyUpdate = false;
    private final Repository<Order> orderRepository;

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
        orderRepository.update(order);
        didManuallyUpdate = true;
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
            didManuallyUpdate = true;
            return currentOrders[state.ordinal()].pop();
        }
    }

    @Override
    public synchronized Order[] getAllOrdersInState(State state) {
        return currentOrders[state.ordinal()].toArray(new Order[0]);
    }

    //REVIEW: Oh my god this seems woefully inefficient
    @Override
    public synchronized boolean requestDataRefresh() {
        ArrayDeque<Order>[] newCurrentOrders = createCurrentOrdersDeque();

        boolean didRefresh = false;
        for (int i = 0; i < State.values().length; i++) {
            // If the number of orders in a state changes between refreshes then either:
            // - An order has changed state
            // - An order was added
            // - An order was externally removed
            if (newCurrentOrders[i].size() != currentOrders[i].size()) {
                didRefresh = true;
                break;
            }
        }

        if (didRefresh)
            currentOrders = newCurrentOrders;

        if (didManuallyUpdate) {
            didManuallyUpdate = false;
            return true;
        }

        return didRefresh;
    }

    private synchronized ArrayDeque<Order>[] createCurrentOrdersDeque() {
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

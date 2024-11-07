package server;

import logic.Order;
import logic.OrderProcessor;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Creates a wrapper around an OrderProcessor instance that allows it to be called and interacted with remotely.
 */
class OrderProcessorRemoteWrapper
        extends UnicastRemoteObject
        implements OrderProcessor {

    @Serial
    private static final long serialVersionUID = 1;

    private final OrderProcessor origin;

    public OrderProcessorRemoteWrapper(OrderProcessor origin) throws RemoteException {
        this.origin = origin;
    }

    @Override
    public synchronized Order createOrder() {
        return origin.createOrder();
    }

    @Override
    public synchronized void addOrderToQueue(Order order) {
        origin.addOrderToQueue(order);
    }

    @Override
    public synchronized Order popOrder() {
        return origin.popOrder();
    }

    @Override
    public synchronized void setOrderState(Order order, State state) {
        origin.setOrderState(order, state);
    }

    @Override
    public Order[] getAllOrdersInState(State state) {
        return origin.getAllOrdersInState(state);
    }
}

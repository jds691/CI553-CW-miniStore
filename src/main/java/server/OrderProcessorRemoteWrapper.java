package server;

import logic.Order;
import logic.OrderProcessor;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static logic.Order.State;

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
    public synchronized Order createOrder() throws RemoteException {
        return origin.createOrder();
    }

    @Override
    public synchronized void addOrderToQueue(Order order) throws RemoteException {
        origin.addOrderToQueue(order);
    }

    @Override
    public synchronized Order popOrder() throws RemoteException {
        return origin.popOrder();
    }

    @Override
    public synchronized Order popOrder(State state) throws RemoteException {
        return origin.popOrder(state);
    }

    @Override
    public synchronized Order[] getAllOrdersInState(State state) throws RemoteException {
        return origin.getAllOrdersInState(state);
    }

    @Override
    public synchronized boolean requestDataRefresh() throws RemoteException {
        return origin.requestDataRefresh();
    }
}

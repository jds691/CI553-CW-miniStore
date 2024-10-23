package remote;

import catalogue.Basket;
import middle.OrderException;
import orders.Order;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

/**
 * The order processing handling.
 * This code is incomplete
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public class R_Order
        extends UnicastRemoteObject
        implements RemoteOrder_I {

    @Serial
    private static final long serialVersionUID = 1;
    private final Order orderProcessor;

    public R_Order(String url) throws RemoteException, OrderException {
        orderProcessor = new Order();
    }

    public void newOrder(Basket bought) throws RemoteException, OrderException {
        orderProcessor.newOrder(bought);
    }

    public int uniqueNumber() throws RemoteException, OrderException {
        return orderProcessor.uniqueNumber();
    }

    public Basket getOrderToPack() throws RemoteException, OrderException {
        return orderProcessor.getOrderToPack();
    }

    public boolean informOrderPacked(int orderNum) throws RemoteException, OrderException {
        return orderProcessor.informOrderPacked(orderNum);
    }

    public boolean informOrderCollected(int orderNum) throws RemoteException, OrderException {
        return orderProcessor.informOrderCollected(orderNum);
    }

    public Map<String, List<Integer>> getOrderState() throws RemoteException, OrderException {
        return orderProcessor.getOrderState();
    }
}

package middle;

import models.Basket;
import debug.DEBUG;
import remote.RemoteOrder_I;

import java.rmi.Naming;
import java.util.List;
import java.util.Map;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

/**
 * Facade for the order processing handling which is implemented on the middle tier.
 * This code is incomplete
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public class F_Order implements OrderProcessor {
    private RemoteOrder_I orderProcessor;
    private final String orderURL;

    public F_Order(String url) {
        orderURL = url;
    }

    private void connect() throws OrderException {
        try {
            orderProcessor = (RemoteOrder_I) Naming.lookup(orderURL);
        } catch (Exception e) {
            orderProcessor = null;

            throw new OrderException("Com: " + e.getMessage());
        }
    }

    public void newOrder(Basket bought) throws OrderException {
        DEBUG.trace("F_Order:newOrder()");

        try {
            if (orderProcessor == null)
                connect();

            orderProcessor.newOrder(bought);
        } catch (Exception e) {
            orderProcessor = null;

            throw new OrderException("Net: " + e.getMessage());
        }
    }

    public int uniqueNumber() throws OrderException {
        DEBUG.trace("F_Order:uniqueNumber()");

        try {
            if (orderProcessor == null)
                connect();

            return orderProcessor.uniqueNumber();
        } catch (Exception e) {
            orderProcessor = null;

            throw new OrderException("Net: " + e.getMessage());
        }
    }

    /**
     * Returns an order to pick from the warehouse
     * if no order then returns null.
     *
     * @return An order to pick
     */
    public synchronized Basket getOrderToPack() throws OrderException {
        DEBUG.trace("F_Order:getOrderTioPack()");

        try {
            if (orderProcessor == null)
                connect();

            return orderProcessor.getOrderToPack();
        } catch (Exception e) {
            orderProcessor = null;

            throw new OrderException("Net: " + e.getMessage());
        }
    }

    /**
     * Informs the order processing system that the order has been
     * picked and the products are now on the conveyor belt to
     * the shop floor.
     */
    public synchronized boolean informOrderPacked(int orderNum) throws OrderException {
        DEBUG.trace("F_Order:informOrderPacked()");

        try {
            if (orderProcessor == null)
                connect();

            return orderProcessor.informOrderPacked(orderNum);
        } catch (Exception e) {
            orderProcessor = null;

            throw new OrderException("Net: " + e.getMessage());
        }
    }

    /**
     * Informs the order processing system that the order has been
     * collected by the customer
     */
    public synchronized boolean informOrderCollected(int orderNum) throws OrderException {
        DEBUG.trace("F_Order:informOrderCollected()");

        try {
            if (orderProcessor == null)
                connect();

            return orderProcessor.informOrderCollected(orderNum);
        } catch (Exception e) {
            orderProcessor = null;

            throw new OrderException("Net: " + e.getMessage());
        }
    }

    /**
     * Returns information about all orders in the order processing system
     */
    public synchronized Map<String, List<Integer>> getOrderState() throws OrderException {
        DEBUG.trace("F_Order:getOrderState()");

        try {
            if (orderProcessor == null)
                connect();

            return orderProcessor.getOrderState();
        } catch (Exception e) {
            orderProcessor = null;

            throw new OrderException("Net: " + e.getMessage());
        }
    }
}

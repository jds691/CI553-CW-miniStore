package logic;

import java.rmi.Remote;

import static logic.Order.State;

/**
 * Responsible for managing and processing orders within the system.
 */
public interface OrderProcessor extends Remote {
    /**
     * Creates a new instance of an order and assigns it a unique order number.
     *
     * @return New Order instance.
     */
    Order createOrder();
    /**
     * Adds an order to the processing queue.
     * <p>
     *     After changing the state of an order. It should be added back to the queue.
     * </p>
     *
     * @param order Order to process.
     */
    void addOrderToQueue(Order order);
    /**
     * Pops an order from the top of the processing queue. May be null.
     *
     * @return Order that needs processed or null.
     */
    Order popOrder();
    /**
     * Pops an order from the top of the processing queue for a specified state. May be null.
     *
     * @return Order that needs processed or null.
     */
    Order popOrder(State state);

    //Querying
    Order[] getAllOrdersInState(State state);

    /**
     * Requests that the OrderProcessor re-syncs itself with the underlying data repository.
     *
     * @return Whether any data has been refreshed or not.
     */
    boolean requestDataRefresh();
}

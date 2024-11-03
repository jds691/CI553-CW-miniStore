package logic;

import java.rmi.Remote;

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
     * Sets the state of an order within the system.
     *
     * @param order Order to update the state of.
     * @param state State to set.
     */
    void setOrderState(Order order, State state);

    /**
     * State of an order within the processing queue.
     */
    enum State {
        WAITING,
        BEING_PACKED,
        TO_BE_COLLECTED
    }
}

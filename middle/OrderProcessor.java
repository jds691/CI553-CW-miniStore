package middle;

import models.Basket;

import java.util.List;
import java.util.Map;

/**
 * Defines the interface for accessing the order processing system.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public interface OrderProcessor {
    void newOrder(Basket bought)
            throws OrderException;

    int uniqueNumber()
            throws OrderException;

    Basket getOrderToPack()
            throws OrderException;

    boolean informOrderPacked(int orderNum)
            throws OrderException;

    boolean informOrderCollected(int orderNum)
            throws OrderException;

    Map<String, List<Integer>> getOrderState()
            throws OrderException;
}

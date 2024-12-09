package clients.packing;

import debug.DEBUG;
import logic.*;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

import static logic.Order.State;

/**
 * Implements the Model of the warehouse packing client
 */
public class PackingModel extends Observable {
    private final AtomicReference<Order[][]> allOrders = new AtomicReference<>();

    private OrderProcessor orderProcessor = null;
    private ProductReader productReader = null;

    private final Semaphore packingWorker = new Semaphore();

    /**
     * Construct the model of the warehouse Packing client
     *
     * @param mf The factory to create the connection objects
     */
    public PackingModel(LogicFactory mf) {
        try {
            orderProcessor = mf.getOrderProcessor();
            productReader = mf.getProductReader();
        } catch (Exception e) {
            DEBUG.error("CustomerModel.constructor\n%s", e.getMessage());
        }

        allOrders.set(null);
        // Start a background check to see when a new order can be packed
        new Thread(this::refreshOrderData).start();
    }

    /**
     * Semaphore used to only allow 1 order
     * to be packed at once by this person
     */
    static class Semaphore {
        private boolean held = false;

        /**
         * Claim exclusive remote.access
         *
         * @return true if claimed else false
         */
        public synchronized boolean claim() {
            return !held && (held = true);
        }

        /**
         * Free the lock
         */
        public synchronized void free() {
            assert held;
            held = false;
        }
    }

    /*
    /**
     * Method run in a separate thread to check if there
     * is a new order waiting to be packed and we have
     * nothing to do.

    private void checkForNewOrder() {
        while (true) {
            try {
                boolean isFree = packingWorker.claim();
                if (isFree) {
                    Order[][] allOrders = new Order[State.values().length][];
                    for (Order.State state : Order.State.values()) {
                        allOrders[state.ordinal()] = orderProcessor.getAllOrdersInState(state);
                    }

                    String prompt = "";
                    if (sb != null) {
                        allOrders.set(sb);
                        prompt = "Bought Receipt";
                    } else {
                        packingWorker.free();
                        prompt = "";
                    }

                    setChanged();
                    notifyObservers(prompt);
                }

                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    */

    private void refreshOrderData() {
        while (true) {
            try {
                if (orderProcessor.requestDataRefresh()) {
                    Order[][] allOrders = new Order[State.values().length][];
                    for (Order.State state : Order.State.values()) {
                        allOrders[state.ordinal()] = orderProcessor.getAllOrdersInState(state);
                    }

                    this.allOrders.set(allOrders);

                    setChanged();
                    notifyObservers("Bought Receipt");
                }

                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Return the Basket of products that are to be picked
     *
     * @return the basket
     */
    public Order[][] getAllOrders() {
        return allOrders.get();
    }

    public double getOrderCost(Order order) {
        double total = 0.0;

        for (Order.Item item : order.getAllItems()) {
            Product product = productReader.getProductDetails(item.getProductNumber());

            total += product.getPrice() * item.getQuantity();
        }

        return total;
    }

    public void updateOrderState(Order order) {
        orderProcessor.addOrderToQueue(order);
    }
}






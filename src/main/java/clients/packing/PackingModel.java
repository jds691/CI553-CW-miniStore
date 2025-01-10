package clients.packing;

import logic.*;

import java.rmi.RemoteException;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

import static logic.Order.State;

/**
 * Implements the Model of the warehouse packing client
 */
public class PackingModel extends Observable {
    private final AtomicReference<Order[][]> allOrders = new AtomicReference<>();

    private final LogicFactory factory;
    private OrderProcessor orderProcessor = null;
    private ProductReader productReader = null;

    /**
     * Construct the model of the warehouse Packing client
     *
     * @param factory The factory to create the connection objects
     */
    public PackingModel(LogicFactory factory) {
        this(factory, true);
    }

    /**
     * Constructs the model of the warehouse Packing client with control over the data refresh thread
     *
     * @apiNote Designed for testing only. Do not call in production code
     */
    public PackingModel(LogicFactory factory, boolean runBackgroundThread) {
        this.factory = factory;
        try {
            orderProcessor = factory.getOrderProcessor();
            productReader = factory.getProductReader();
        } catch (Exception e) {
            System.err.println("Unable to create packing model");
            throw new RuntimeException(e);
        }

        allOrders.set(null);
        // Start a background check to see when a new order can be packed
        if (runBackgroundThread)
            new Thread(this::refreshOrderData).start();
    }

    public LogicFactory getFactory() {
        return this.factory;
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

    private void refreshOrderData() {
        while (true) {
            try {
                requestOrderDataRefresh();

                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void requestOrderDataRefresh() {
        try {
            boolean didRefresh = orderProcessor.requestDataRefresh();
            if (didRefresh || allOrders.get() == null) {
                Order[][] allOrders = new Order[State.values().length][];
                for (Order.State state : Order.State.values()) {
                    allOrders[state.ordinal()] = orderProcessor.getAllOrdersInState(state);
                }

                this.allOrders.set(allOrders);

                setChanged();
                notifyObservers();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            Product product = null;
            try {
                product = productReader.getProductDetails(item.getProductNumber());
            } catch (RemoteException e) {
                System.err.println("RemoteException: " + e.getMessage());
                setChanged();
                notifyObservers("Unable to connect to server");
                return -1;
            }

            total += product.getPrice() * item.getQuantity();
        }

        return total;
    }

    public void updateOrderState(Order order) {
        try {
            orderProcessor.addOrderToQueue(order);
        } catch (RemoteException e) {
            System.err.println("RemoteException: " + e.getMessage());
            setChanged();
            notifyObservers("Unable to connect to server");
        }
    }
}






package clients.packing;

import catalogue.Basket;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderException;
import middle.OrderProcessor;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implements the Model of the warehouse packing client
 */
public class PackingModel extends Observable {
    private final AtomicReference<Basket> basket = new AtomicReference<>();

    private OrderProcessor orderProcessor = null;

    private final Semaphore worker = new Semaphore();

    /**
     * Construct the model of the warehouse Packing client
     *
     * @param mf The factory to create the connection objects
     */
    public PackingModel(MiddleFactory mf) {
        try {
            orderProcessor = mf.makeOrderProcessor();
        } catch (Exception e) {
            DEBUG.error("CustomerModel.constructor\n%s", e.getMessage());
        }

        basket.set(null);
        // Start a background check to see when a new order can be packed
        new Thread(this::checkForNewOrder).start();
    }

    /**
     * Semaphore used to only allow 1 order
     * to be packed at once by this person
     */
    static class Semaphore {
        private boolean held = false;

        /**
         * Claim exclusive access
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

    /**
     * Method run in a separate thread to check if there
     * is a new order waiting to be packed and we have
     * nothing to do.
     */
    private void checkForNewOrder() {
        while (true) {
            try {
                boolean isFree = worker.claim();
                if (isFree) {
                    Basket sb = orderProcessor.getOrderToPack();
                    String prompt = "";
                    if (sb != null) {
                        basket.set(sb);
                        prompt = "Bought Receipt";
                    } else {
                        worker.free();
                        prompt = "";
                    }

                    setChanged();
                    notifyObservers(prompt);
                }

                Thread.sleep(2000);
            } catch (Exception e) {
                DEBUG.error("%s\n%s", "BackGroundCheck.run()\n%s", e.getMessage());
            }
        }
    }

    /**
     * Return the Basket of products that are to be picked
     *
     * @return the basket
     */
    public Basket getBasket() {
        return basket.get();
    }

    /**
     * Process a packed Order
     */
    public void packOrder() {
        String prompt = "";

        try {
            Basket basket = this.basket.get();
            if (basket != null) {
                this.basket.set(null);
                int orderNumber = basket.getOrderNumber();
                orderProcessor.informOrderPacked(orderNumber);
                prompt = "";
                worker.free();
            } else {
                prompt = "No order";
            }
            setChanged();
            notifyObservers(prompt);
        } catch (OrderException e) {
            DEBUG.error("ReceiptModel.doOk()\n%s\n", e.getMessage());
        }

        setChanged();
        notifyObservers(prompt);
    }
}






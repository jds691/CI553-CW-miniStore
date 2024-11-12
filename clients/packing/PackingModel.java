package clients.packing;

import debug.DEBUG;
import logic.*;

import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

import static logic.Order.State;

/**
 * Implements the Model of the warehouse packing client
 */
public class PackingModel extends Observable {
    private final AtomicReference<Order> currentOrder = new AtomicReference<>();

    private OrderProcessor orderProcessor = null;
    private ProductReader productReader = null;

    private final Semaphore worker = new Semaphore();

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

        currentOrder.set(null);
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
                    Order sb = orderProcessor.popOrder();
                    String prompt = "";
                    if (sb != null) {
                        currentOrder.set(sb);
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
                e.printStackTrace();
            }
        }
    }

    /**
     * Return the Basket of products that are to be picked
     *
     * @return the basket
     */
    public Order getCurrentOrder() {
        return currentOrder.get();
    }

    /**
     * Process a packed Order
     */
    public void packOrder() {
        String prompt = "";

        Order order = this.currentOrder.get();
        if (order != null) {
            this.currentOrder.set(null);
            order.setState(State.BEING_PACKED);
            orderProcessor.addOrderToQueue(order);
            prompt = "";
            worker.free();
        } else {
            prompt = "No order";
        }
        setChanged();
        notifyObservers(prompt);

        setChanged();
        notifyObservers(prompt);
    }

    public String getOrderDescription() {
        Locale locale = Locale.UK;
        StringBuilder stringBuilder = new StringBuilder(256);
        Formatter formatter = new Formatter(stringBuilder, locale);
        String currencySymbol = (Currency.getInstance(locale)).getSymbol();
        double total = 0.00;
        Order order = this.currentOrder.get();

        if (order.getOrderNumber() != 0)
            formatter.format("Order number: %03d\n", order.getOrderNumber());

        if (!order.isEmpty()) {
            for (Order.Item item : order.getAllItems()) {
                Product product = productReader.getProductDetails(item.getProductNumber());
                formatter.format("%-7s", product.getProductNumber());
                formatter.format("%-14.14s ", product.getDescription());
                formatter.format("(%3d) ", item.getQuantity());
                formatter.format("%s%7.2f", currencySymbol, product.getPrice() * item.getQuantity());
                formatter.format("\n");
                total += product.getPrice() * item.getQuantity();
            }

            formatter.format("----------------------------\n");
            formatter.format("Total                       ");
            formatter.format("%s%7.2f\n", currencySymbol, total);
            formatter.close();
        }

        return stringBuilder.toString();
    }
}






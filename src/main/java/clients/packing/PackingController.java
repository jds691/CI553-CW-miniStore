package clients.packing;

import logic.LogicFactory;
import logic.Order;

/**
 * The Packing Controller
 */
public class PackingController {
    private final PackingModel model;

    /**
     * Constructor
     *
     * @param model The model
     */
    public PackingController(PackingModel model) {
        this.model = model;
    }

    public LogicFactory getFactory() {
        return model.getFactory();
    }

    public double getOrderCost(Order order) {
        return model.getOrderCost(order);
    }

    public void updateOrderState(Order order) {
        model.updateOrderState(order);
    }

    public Order[][] getAllOrders() {
        return model.getAllOrders();
    }
}


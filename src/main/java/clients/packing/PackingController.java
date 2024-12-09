package clients.packing;

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

    /**
     * Picked interaction from view
     */
    public void packOrder() {
        model.packOrder();
    }

    //REVIEW: Will be removed in a future iteration
    public String getOrderDescription() {
        return model.getOrderDescription();
    }

    public double getOrderCost(Order order) {
        return model.getOrderCost(order);
    }

    public void updateOrderState(Order order) {
        model.updateOrderState(order);
    }
}


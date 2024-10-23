package clients.packing;

/**
 * The Packing Controller
 */
public class PackingController {
    private final PackingModel model;

    /**
     * Constructor
     *
     * @param model The model
     * @param view  The view from which the interaction came
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
}


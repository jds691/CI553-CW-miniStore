package clients.packing;

/**
 * The Packing Controller
 */
public class PackingController {
    private PackingModel model;
    private PackingView view;

    /**
     * Constructor
     *
     * @param model The model
     * @param view  The view from which the interaction came
     */
    public PackingController(PackingModel model, PackingView view) {
        this.view = view;
        this.model = model;
    }

    /**
     * Picked interaction from view
     */
    public void doPacked() {
        model.doPacked();
    }
}


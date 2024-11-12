package clients.customer;

/**
 * The Customer Controller
 */

public class CustomerController {
    private final CustomerModel model;

    /**
     * Constructor
     *
     * @param model The model
     * @param view  The view from which the interaction came
     */
    public CustomerController(CustomerModel model) {
        this.model = model;
    }

    /**
     * Check interaction from view
     *
     * @param productNumber The product number to be checked
     */
    public void doCheck(String productNumber) {
        model.queryProduct(productNumber);
    }

    /**
     * Clear interaction from view
     */
    public void reset() {
        model.reset();
    }
}


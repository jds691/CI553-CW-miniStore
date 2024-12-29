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
     */
    public CustomerController(CustomerModel model) {
        this.model = model;
    }

    /**
     * Check interaction from view
     *
     * @param productNumber The product number to be checked
     */
    public void queryProduct(String productNumber) {
        model.queryProduct(productNumber);
    }

    public String getProductName() {
        return model.getProductName();
    }

    public String getProductMetadata() {
        return model.getProductMetadata();
    }

    public String getProductDescription() {
        return model.getProductDescription();
    }
}


package clients.backDoor;

/**
 * The BackDoor Controller
 */
public class BackDoorController {
    private final BackDoorModel model;

    /**
     * Constructor
     *
     * @param model The model
     */
    public BackDoorController(BackDoorModel model) {
        this.model = model;
    }

    /**
     * Query interaction from view
     *
     * @param productNumber The product number to be checked
     */
    public void queryProduct(String productNumber) {
        model.queryProduct(productNumber);
    }

    /**
     * RStock interaction from view
     *
     * @param productNumber       The product number to be re-stocked
     * @param quantity The quantity to be re-stocked
     */
    public void restockProduct(String productNumber, String quantity) throws NumberFormatException {
        quantity = quantity.trim();

        int numericQuantity = Integer.parseInt(quantity);

        if (numericQuantity < 1) {
            throw new NumberFormatException("Quantity must be greater than 0");
        }

        model.restockProduct(productNumber, numericQuantity);
    }

    /**
     * Clear interaction from view
     */
    public void reset() {
        model.reset();
    }
}


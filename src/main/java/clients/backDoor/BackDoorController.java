package clients.backDoor;

import java.beans.PropertyChangeListener;

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
    public void restockProduct(String productNumber, int quantity) throws NumberFormatException {
        if (quantity < 1) {
            throw new NumberFormatException("Quantity must be greater than 0");
        }

        model.restockProduct(productNumber, quantity);
    }

    /**
     * Clear interaction from view
     */
    public void reset() {
        model.reset();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        model.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        model.removePropertyChangeListener(listener);
    }
}


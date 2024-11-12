package clients.cashier;

/**
 * The Cashier Controller
 */
public class CashierController {
    private final CashierModel model;

    /**
     * Constructor
     *
     * @param model The model
     */
    public CashierController(CashierModel model) {
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

    /**
     * Buy interaction from view
     */
    public void buyCurrentProduct() {
        model.buyCurrentProduct();
    }

    /**
     * Bought interaction from view
     */
    public void buyBasket() {
        model.buyBasket();
    }

    public String getOrderDescription() {
        return model.getOrderDescription();
    }
}

package catalogue;

import java.io.Serial;
import java.io.Serializable;

/**
 * Used to hold the following information about
 * a product: Product number, Description, Price, Stock level.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public class Product implements Serializable {
    @Serial
    private static final long serialVersionUID = 20092506;
    private String productNumber;
    private String description;
    private double price;
    private int quantity;

    /**
     * Construct a product details
     *
     * @param productNumber  Product number
     * @param description Description of product
     * @param price       The price of the product
     * @param quantity    The Quantity of the product involved
     */
    public Product(String productNumber, String description, double price, int quantity) {
        this.productNumber = productNumber;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String value) {
        productNumber = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        description = value;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double value) {
        price = value;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int value) {
        quantity = value;
    }
}

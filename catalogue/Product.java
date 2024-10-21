package catalogue;

import java.io.Serializable;

/**
 * Used to hold the following information about
 * a product: Product number, Description, Price, Stock level.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */

public class Product implements Serializable {
    private static final long serialVersionUID = 20092506;
    private String theProductNum;
    private String theDescription;
    private double thePrice;
    private int theQuantity;

    /**
     * Construct a product details
     *
     * @param aProductNum  Product number
     * @param aDescription Description of product
     * @param aPrice       The price of the product
     * @param aQuantity    The Quantity of the product involved
     */
    public Product(String aProductNum, String aDescription, double aPrice, int aQuantity) {
        theProductNum = aProductNum;
        theDescription = aDescription;
        thePrice = aPrice;
        theQuantity = aQuantity;
    }

    public String getProductNum() {
        return theProductNum;
    }

    public void setProductNum(String aProductNum) {
        theProductNum = aProductNum;
    }

    public String getDescription() {
        return theDescription;
    }

    public void setDescription(String aDescription) {
        theDescription = aDescription;
    }

    public double getPrice() {
        return thePrice;
    }

    public void setPrice(double aPrice) {
        thePrice = aPrice;
    }

    public int getQuantity() {
        return theQuantity;
    }

    public void setQuantity(int aQuantity) {
        theQuantity = aQuantity;
    }
}

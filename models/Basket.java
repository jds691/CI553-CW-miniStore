package models;

import logic.Product;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;

/**
 * A collection of products,
 * used to record the products that are to be wished to be purchased.
 *
 * @author Mike Smith University of Brighton
 * @version 2.2
 */
@Deprecated(forRemoval = true)
public class Basket extends ArrayList<Product> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;
    private int orderNumber;

    /**
     * Constructor for a basket which is
     * used to represent a customer order/ wish list
     */
    public Basket() {
        orderNumber = 0;
    }

    /**
     * Returns the customers unique order number
     *
     * @return the customers order number
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * Set the customers unique order number
     * Valid order Numbers 1 .. N
     *
     * @param value A unique order number
     */
    public void setOrderNumber(int value) {
        this.orderNumber = value;
    }

    /**
     * Add a product to the Basket.
     * Product is appended to the end of the existing products
     * in the basket.
     *
     * @param product A product to be added to the basket
     * @return true if successfully adds the product
     */
    @Override
    public boolean add(Product product) {
        return super.add(product);
    }

    /**
     * Returns a description of the products in the basket suitable for printing.
     *
     * @return a string description of the basket products
     */
    public String getDetails() {
        Locale locale = Locale.UK;
        StringBuilder stringBuilder = new StringBuilder(256);
        Formatter formatter = new Formatter(stringBuilder, locale);
        String currencySymbol = (Currency.getInstance(locale)).getSymbol();
        double total = 0.00;

        if (orderNumber != 0)
            formatter.format("Order number: %03d\n", orderNumber);

        if (this.size() > 0) {
            for (Product product : this) {
                int number = product.getQuantity();
                formatter.format("%-7s", product.getProductNumber());
                formatter.format("%-14.14s ", product.getDescription());
                formatter.format("(%3d) ", number);
                formatter.format("%s%7.2f", currencySymbol, product.getPrice() * number);
                formatter.format("\n");
                total += product.getPrice() * number;
            }

            formatter.format("----------------------------\n");
            formatter.format("Total                       ");
            formatter.format("%s%7.2f\n", currencySymbol, total);
            formatter.close();
        }

        return stringBuilder.toString();
    }
}

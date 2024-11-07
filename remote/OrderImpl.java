package remote;

import logic.Order;
import logic.Product;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;

class OrderImpl implements Order {
    private final ArrayList<Product> products = new ArrayList<>();
    private int orderNumber = 0;

    @Override
    public int getOrderNumber() {
        return orderNumber;
    }

    @Override
    public void setOrderNumber(int value) {
        orderNumber = value;
    }

    @Override
    public void addProduct(Product product) {
        //TODO: Update product quantity if products contains it already, then sort based on productNumber
        products.add(product);
    }

    @Override
    public void removeProduct(Product product) {
        products.remove(product);
    }

    @Override
    public void removeAllProducts() {
        products.clear();
    }

    @Override
    public boolean containsProduct(Product product) {
        return products.contains(product);
    }

    @Override
    public boolean isEmpty() {
        return products.isEmpty();
    }

    @Override
    public String getRichDescription() {
        Locale locale = Locale.UK;
        StringBuilder stringBuilder = new StringBuilder(256);
        Formatter formatter = new Formatter(stringBuilder, locale);
        String currencySymbol = (Currency.getInstance(locale)).getSymbol();
        double total = 0.00;

        if (orderNumber != 0)
            formatter.format("Order number: %03d\n", orderNumber);

        if (!products.isEmpty()) {
            for (Product product : products) {
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

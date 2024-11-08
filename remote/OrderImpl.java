package remote;

import logic.Order;
import logic.Product;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;

class OrderImpl implements Order {
    private final ArrayList<Item> items = new ArrayList<>();
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
    public void addItem(Item item) {
        //TODO: Sort based on productNumber
        if (items.contains(item)) {
            int index = items.indexOf(item);
            Item original = items.get(index);
            original.setQuantity(original.getQuantity() + item.getQuantity());
            items.set(index, original);
        } else {
            items.add(item);
        }
    }

    @Override
    public void removeItem(Item item) {
        items.remove(item);
    }

    @Override
    public void removeAllItems() {
        items.clear();
    }

    @Override
    public boolean containsProduct(Product product) {
        return items.contains(new Item(product, -1));
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
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

        if (!items.isEmpty()) {
            for (Item item : items) {
                Product product = item.getProduct();

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

    //Internal
    /**
     * Gets the underlying items array for the Order.
     * <p>
     *     Should only be called internally to the remote package!
     * </p>
     *
     * @return ArrayList of items in order
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * Sets the underlying items array for the Order.
     *
     * <p>
     *     Should only be called by the {@link OrderRepository}!
     * </p>
     *
     * @param items Items to set
     */
    public void setItems(ArrayList<Item> items) {
        this.items.clear();
        this.items.addAll(items);
    }
}

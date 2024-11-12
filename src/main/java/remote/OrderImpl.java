package remote;

import logic.Order;
import logic.Product;

import java.util.ArrayList;

class OrderImpl implements Order {
    private final ArrayList<Item> items = new ArrayList<>();
    private int orderNumber = 0;
    private State state = State.WAITING;

    @Override
    public int getOrderNumber() {
        return orderNumber;
    }

    @Override
    public void setOrderNumber(int value) {
        orderNumber = value;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State value) {
        this.state = value;
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
        return items.contains(new Item(product.getProductNumber(), -1));
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Gets the underlying items array for the Order for recursion.
     *
     * @return Array of items in order
     */
    @Override
    public Item[] getAllItems() {
        return items.toArray(new Item[0]);
    }
}

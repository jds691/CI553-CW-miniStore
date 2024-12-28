package logic;

public interface Order {
    int getOrderNumber();
    void setOrderNumber(int value);

    State getState();
    void setState(State value);

    /**
     * Adds a product and it's desired quantity to the order.
     *
     * <p>
     *     If the order already contains an item with the same product, the quantities are added together.
     * </p>
     *
     * @param item Item to add to order.
     */
    void addItem(Item item);
    /**
     * Removes the specified item from the order
     *
     * @param item Item to remove
     */
    void removeItem(Item item);
    /**
     * Finds an instance of {@link Order.Item} in the order for a given product number
     *
     * @param productNumber Product number linked to this item
     * @return Item if it was found or null
     */
    Item getItem(String productNumber);
    /**
     * Updates the quantity of the item within the order whilst maintaining its index position
     *
     * @param item Item to update
     */
    void updateItem(Item item);
    /**
     * Removes all items from the order
     */
    void removeAllItems();
    /**
     * Returns whether the order contains any items or not
     *
     * @return If order contains items
     */
    boolean isEmpty();
    /**
     * Gets the underlying items collection for the Order for recursion.
     *
     * @return Collection of items in order
     */
    Item[] getAllItems();

    /**
     * State of an order within the processing queue.
     */
    enum State {
        WAITING,
        BEING_PACKED,
        TO_BE_COLLECTED
    }

    class Item {
        private String productNumber;
        private int quantity;

        public Item(String productNumber, int quantity) {
            this.productNumber = productNumber;
            this.quantity = quantity;
        }

        public String getProductNumber() {
            return productNumber;
        }
        public void setProductNumber(String productNumber) {
            this.productNumber = productNumber;
        }

        public int getQuantity() {
            return quantity;
        }
        public void setQuantity(int value) {
            this.quantity = value;
        }

        //Always returns true if both items contain the same product
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Item item) {
                return this.productNumber.equals(item.productNumber);
            }

            return false;
        }
    }
}

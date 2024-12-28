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
    void removeItem(Item item);
    Item getItem(String productNumber);
    void updateItem(Item item);
    void removeAllItems();
    boolean isEmpty();

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

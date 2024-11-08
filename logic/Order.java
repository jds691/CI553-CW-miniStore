package logic;

public interface Order {
    int getOrderNumber();
    void setOrderNumber(int value);

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
    void removeAllItems();
    boolean containsProduct(Product product);
    boolean isEmpty();

    /**
     * Returns a description of the products in the basket suitable for printing.
     *
     * @return a string description of the basket products
     */
    String getRichDescription();

    class Item {
        private Product product;
        private int quantity;

        public Item(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }
        public void setProduct(Product product) {
            this.product = product;
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
                return this.product.equals(item.product);
            }

            return false;
        }
    }
}

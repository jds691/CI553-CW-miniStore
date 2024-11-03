package logic;

public interface Order {
    int getOrderNumber();
    void setOrderNumber(int value);

    void addProduct(Product product);
    void removeProduct(Product product);
    void removeAllProducts();
    boolean containsProduct(Product product);
    boolean isEmpty();

    /**
     * Returns a description of the products in the basket suitable for printing.
     *
     * @return a string description of the basket products
     */
    String getRichDescription();
}

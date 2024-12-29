package logic;

public interface Product {
    String getProductNumber();
    void setProductNumber(String value);

    String getName();
    void setName(String value);

    String getDescription();
    void setDescription(String value);

    double getPrice();
    void setPrice(double value);

    /**
     * Gets the current number of this product in stock.
     *
     * @return Number of product in stock.
     */
    int getQuantity();
    void setQuantity(int value);

    String getImageFilename();
}

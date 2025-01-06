package remote;

import logic.Product;

import java.io.Serializable;

class ProductImpl implements Product, Serializable {
    private String productNumber;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String imageFilename;

    public ProductImpl(String productNumber, String name, String description, double price, int quantity, String imageFilename) {
        this.productNumber = productNumber;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.imageFilename = imageFilename;
    }

    @Override
    public String getProductNumber() {
        return productNumber;
    }

    @Override
    public void setProductNumber(String value) {
        productNumber = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String value) {
        name = value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String value) {
        description = value;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double value) {
        price = value;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int value) {
        quantity = value;
    }

    @Override
    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductImpl product) {
            return product.getProductNumber().equals(this.getProductNumber());
        }

        return false;
    }
}

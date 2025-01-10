package facade;

import logic.Product;

import java.io.Serializable;

public class FacadeProduct implements Product, Serializable {
    private String productNumber;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String imageFilename;

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
        if (obj instanceof FacadeProduct product) {
            return product.getProductNumber().equals(this.getProductNumber());
        }

        return false;
    }
}

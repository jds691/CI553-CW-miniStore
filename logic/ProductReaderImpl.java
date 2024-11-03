package logic;

import javax.swing.*;

public class ProductReaderImpl implements ProductReader {
    @Override
    public boolean doesProductExist(String productNumber) {
        return false;
    }

    @Override
    public Product getProductDetails(String pNum) {
        return null;
    }

    @Override
    public ImageIcon getProductImage(String pNum) {
        return null;
    }
}

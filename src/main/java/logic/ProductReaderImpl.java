package logic;

import remote.Repository;

import javax.swing.*;

class ProductReaderImpl implements ProductReader {
    private final Repository<Product> productRepository;

    public ProductReaderImpl(Repository<Product> repository) {
        this.productRepository = repository;
    }

    @Override
    public boolean doesProductExist(String productNumber) {
        return productRepository.read(productNumber) != null;
    }

    @Override
    public Product getProductDetails(String productNumber) {
        return productRepository.read(productNumber);
    }

    @Override
    public ImageIcon getProductImage(String productNumber) {
        String filename = productRepository.read(productNumber).getImageFilename();

        if (filename.isEmpty()) {
            return new ImageIcon("default.jpg");
        } else {
            return new ImageIcon(filename);
        }
    }
}

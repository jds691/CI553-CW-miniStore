package logic;

import remote.Repository;

import javax.swing.*;
import java.nio.file.Path;

class ProductReaderImpl implements ProductReader {
    private final Repository<Product> productRepository;

    public ProductReaderImpl(Repository<Product> repository) {
        this.productRepository = repository;
    }

    @Override
    public boolean doesProductExist(String productNumber) {
        return !productRepository.read(productNumber).getProductNumber().equals("0");
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
            //TODO: Move images into resources or something to remove the need for this
            return new ImageIcon(Path.of("src", "main", "java", filename).toString());
        }
    }

    @Override
    public Repository<Product> getRepository() {
        return productRepository;
    }
}

package logic;

import remote.Repository;

class StockWriterImpl implements StockWriter {
    private final Repository<Product, String> stockRepository;

    public StockWriterImpl(Repository<Product, String> stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public synchronized boolean buyStock(Product product, int amount) {
        product.setQuantity(product.getQuantity() - amount);
        return stockRepository.update(product);
    }

    @Override
    public synchronized void addStock(Product product, int amount) {
        product.setQuantity(product.getQuantity() + amount);
        stockRepository.update(product);
    }

    @Override
    public synchronized void modifyStock(Product product) {
        stockRepository.update(product);
    }
}

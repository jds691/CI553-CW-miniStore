package facade;

import logic.Product;

public class FacadeProductRepository extends FacadeRepository<Product> {
    @Override
    public Product create() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Product read(String id) {
        FacadeProduct defaultProduct = new FacadeProduct();

        try {
            return (Product) entities.stream().filter(x -> x.getProductNumber().equals(id)).toArray()[0];
        } catch (Exception e) {
            return defaultProduct;
        }
    }

    @Override
    public Product[] readAll() {
        return entities.toArray(new Product[0]);
    }

    @Override
    public boolean update(Product product) {
        read(product.getProductNumber()).setQuantity(product.getQuantity());

        return true;
    }

    @Override
    public void delete(Product product) {
        remove(product);
    }
}

package clients.customer;

import facade.FacadeProduct;
import facade.FacadeProductRepository;
import facade.FacadeRepositoryFactory;
import logic.LocalLogicFactory;
import logic.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import remote.RepositoryFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CustomerModelTest {
    private static Product product;
    private static CustomerModel customerModel;

    @BeforeAll
    static void setUp() {
        customerModel = new CustomerModel(new LocalLogicFactory(generateRepositoryFactory()));
    }

    @Test
    void queryProduct() {
        customerModel.queryProduct(product.getProductNumber());
        assertEquals(product.getName(), customerModel.getProductName());

        customerModel.queryProduct("0002");
        assertNull(customerModel.getProductName());
    }

    static RepositoryFactory generateRepositoryFactory() {
        FacadeRepositoryFactory facadeRepositoryFactory = new FacadeRepositoryFactory();

        product = new FacadeProduct();
        product.setProductNumber("0001");
        product.setName("TV");

        FacadeProductRepository productRepository = new FacadeProductRepository();
        productRepository.add(product);
        facadeRepositoryFactory.setProductRepository(productRepository);

        return facadeRepositoryFactory;
    }
}
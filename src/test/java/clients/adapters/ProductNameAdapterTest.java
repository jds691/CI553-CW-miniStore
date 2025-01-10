package clients.adapters;

import facade.FacadeProduct;
import facade.FacadeProductRepository;
import facade.FacadeRepositoryFactory;
import logic.LocalLogicFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import remote.RepositoryFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
class ProductNameAdapterTest {

    private ProductNameAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ProductNameAdapter(new LocalLogicFactory(generateRepositoryFactory()).getProductReader());
    }

    @AfterEach
    void tearDown() {
        adapter = null;
    }

    @Test
    void getProductNumber() {
        assertEquals("0001", adapter.getProductNumber("Thermometer"));
        assertEquals("0001", adapter.getProductNumber("The"));
        assertNull(adapter.getProductNumber("Fridge"));
    }

    private RepositoryFactory generateRepositoryFactory() {
        FacadeRepositoryFactory factory = new FacadeRepositoryFactory();

        FacadeProduct thermometer = new FacadeProduct();
        thermometer.setProductNumber("0001");
        thermometer.setName("Thermometer");

        FacadeProductRepository productRepository = new FacadeProductRepository();
        productRepository.add(thermometer);

        factory.setProductRepository(productRepository);

        return factory;
    }
}
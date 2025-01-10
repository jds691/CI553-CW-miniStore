package clients.cashier;

import facade.FacadeProduct;
import facade.FacadeProductRepository;
import facade.FacadeRepositoryFactory;
import logic.LocalLogicFactory;
import logic.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import remote.RepositoryFactory;

import static org.junit.jupiter.api.Assertions.*;

class CashierModelTest {
    private CashierModel model;
    private FacadeProduct firstProduct;
    private FacadeProduct secondProduct;

    private RepositoryFactory repositoryFactory;

    @BeforeEach
    void setUp() {
        repositoryFactory = generateRepositoryFactory();
        model = new CashierModel(new LocalLogicFactory(repositoryFactory));
    }

    @AfterEach
    void tearDown() {
        repositoryFactory = null;
        model = null;
    }

    @Test
    void getCurrentOrder() {
        assertNull(model.getCurrentOrder());

        model.queryProduct(firstProduct.getProductNumber());
        model.buyCurrentProduct(1);
        assertNotNull(model.getCurrentOrder());
    }

    @Test
    void updateOrderItem() {
        model.queryProduct(secondProduct.getProductNumber());
        model.buyCurrentProduct(1);
        Order.Item item = model.getCurrentOrder().getAllItems()[0];
        item.setQuantity(2);

        model.updateOrderItem(item);

        assertEquals(2, model.getCurrentOrder().getItem(secondProduct.getProductNumber()).getQuantity());
    }

    @Test
    void removeOrderItem() {
        model.queryProduct(firstProduct.getProductNumber());
        model.buyCurrentProduct(1);
        assertEquals(1, model.getCurrentOrder().getAllItems().length);

        model.removeOrderItem(model.getCurrentOrder().getItem(firstProduct.getProductNumber()));
        assertEquals(0, model.getCurrentOrder().getAllItems().length);
    }

    @Test
    void getCurrentProduct() {
        model.queryProduct(firstProduct.getProductNumber());
        assertEquals(firstProduct, model.getCurrentProduct());
    }

    @Test
    void clearCurrentOrder() {
        model.queryProduct(firstProduct.getProductNumber());
        model.buyCurrentProduct(1);
        model.clearCurrentOrder();

        assertNull(model.getCurrentOrder());
    }

    @Test
    void queryProduct() {
        model.queryProduct(firstProduct.getProductNumber());
        assertEquals(firstProduct, model.getCurrentProduct());
    }

    @Test
    void buyCurrentProduct() {
        model.queryProduct(firstProduct.getProductNumber());
        model.buyCurrentProduct(1);
        assertEquals(1, model.getCurrentOrder().getAllItems().length);
    }

    @Test
    void buyBasket() {
        model.queryProduct(firstProduct.getProductNumber());
        model.buyCurrentProduct(1);
        model.buyBasket();

        assertNotNull(repositoryFactory.getOrderRepository().read("1"));
    }

    private RepositoryFactory generateRepositoryFactory() {
        FacadeRepositoryFactory factory = new FacadeRepositoryFactory();

        firstProduct = new FacadeProduct();
        firstProduct.setProductNumber("0001");
        firstProduct.setName("First");
        firstProduct.setDescription("First product");
        firstProduct.setPrice(1.0);
        firstProduct.setQuantity(1);
        firstProduct.setImageFilename("first");

        secondProduct = new FacadeProduct();
        secondProduct.setProductNumber("0002");
        secondProduct.setName("Second");
        secondProduct.setDescription("Second product");
        secondProduct.setPrice(2.0);
        secondProduct.setQuantity(2);
        secondProduct.setImageFilename("second");

        FacadeProductRepository productRepository = new FacadeProductRepository();
        productRepository.add(firstProduct);
        productRepository.add(secondProduct);

        factory.setProductRepository(productRepository);

        return factory;
    }
}
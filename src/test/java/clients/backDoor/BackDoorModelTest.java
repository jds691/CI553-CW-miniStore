package clients.backDoor;

import facade.FacadeProduct;
import facade.FacadeProductRepository;
import facade.FacadeRepositoryFactory;
import logic.LocalLogicFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import remote.RepositoryFactory;

import java.util.Observable;
import java.util.Observer;

import static org.junit.jupiter.api.Assertions.assertEquals;
class BackDoorModelTest implements Observer {
    private String lastPrompt;
    private BackDoorModel model;
    private RepositoryFactory repositoryFactory;

    @BeforeEach
    void setUp() {
        repositoryFactory = generateRepositoryFactory();
        model = new BackDoorModel(new LocalLogicFactory(repositoryFactory));
        model.addObserver(this);
    }

    @AfterEach
    void tearDown() {
        model.deleteObserver(this);
        model = null;
        lastPrompt = null;
    }

    @Test
    void queryProduct() {
        model.queryProduct("0001");
        assertEquals("TV :  100.00 ( 1) ", lastPrompt);

        model.queryProduct("0002");
        assertEquals("Unknown product number 0002", lastPrompt);
    }

    @Test
    void restockProduct() {
        model.restockProduct("0001", 1);
        assertEquals(2, repositoryFactory.getProductRepository().read("0001").getQuantity());
    }

    @Test
    void reset() {
        model.restockProduct("0001", 1);

        model.reset();

        assertEquals("Enter Product Number", lastPrompt);
        assertEquals("History:\n\n", model.getHistory());
    }

    @Test
    void getHistory() {
        model.restockProduct("0001", 1);

        assertEquals("History:\n\nTV: (+1)\n", model.getHistory());
    }

    @Override
    public void update(Observable model, Object arg) {
        lastPrompt = (String) arg;
    }

    private RepositoryFactory generateRepositoryFactory() {
        FacadeRepositoryFactory facadeRepositoryFactory = new FacadeRepositoryFactory();

        FacadeProduct quantityProduct = new FacadeProduct();
        quantityProduct.setProductNumber("0001");
        quantityProduct.setName("TV");
        quantityProduct.setQuantity(1);
        quantityProduct.setPrice(100.00);

        FacadeProductRepository productRepository = new FacadeProductRepository();
        productRepository.add(quantityProduct);

        facadeRepositoryFactory.setProductRepository(productRepository);

        return facadeRepositoryFactory;
    }
}
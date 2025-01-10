package clients.packing;

import facade.FacadeOrder;
import facade.FacadeProduct;
import facade.FacadeProductRepository;
import facade.FacadeRepositoryFactory;
import logic.LocalLogicFactory;
import logic.LogicFactory;
import logic.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import remote.RepositoryFactory;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PackingModelTest {
    private PackingModel model;
    private LogicFactory logicFactory;
    private RepositoryFactory repositoryFactory;

    @BeforeEach
    void setUp() {
        repositoryFactory = generateRepositoryFactory();
        logicFactory = new LocalLogicFactory(repositoryFactory);
        model = new PackingModel(logicFactory, false);
    }

    @AfterEach
    void tearDown() {
        model = null;
        logicFactory = null;
        repositoryFactory = null;
    }

    @Test
    void getAllOrders() {
        try {
            model.requestOrderDataRefresh();
            assertEquals(0, model.getAllOrders()[Order.State.WAITING.ordinal()].length);

            Order order = logicFactory.getOrderProcessor().createOrder();
            logicFactory.getOrderProcessor().addOrderToQueue(order);

            model.requestOrderDataRefresh();
            assertEquals(1, model.getAllOrders()[Order.State.WAITING.ordinal()].length);

            order = logicFactory.getOrderProcessor().popOrder();
            order.setState(Order.State.BEING_PACKED);
            logicFactory.getOrderProcessor().addOrderToQueue(order);

            model.requestOrderDataRefresh();
            assertEquals(0, model.getAllOrders()[Order.State.WAITING.ordinal()].length);
            assertEquals(1, model.getAllOrders()[Order.State.BEING_PACKED.ordinal()].length);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getOrderCost() {
        FacadeOrder order = new FacadeOrder();
        order.setOrderNumber(1);

        order.addItem(new Order.Item("0001", 1));
        order.addItem(new Order.Item("0002", 20));

        assertEquals(110, model.getOrderCost(order));
    }

    private RepositoryFactory generateRepositoryFactory() {
        FacadeRepositoryFactory factory = new FacadeRepositoryFactory();

        FacadeProduct firstProduct = new FacadeProduct();
        firstProduct.setName("first");
        firstProduct.setProductNumber("0001");
        firstProduct.setPrice(10.00);

        FacadeProduct secondProduct = new FacadeProduct();
        secondProduct.setName("second");
        secondProduct.setProductNumber("0002");
        secondProduct.setPrice(5.00);

        FacadeProductRepository productRepository = new FacadeProductRepository();
        productRepository.add(firstProduct);
        productRepository.add(secondProduct);

        factory.setProductRepository(productRepository);

        return factory;
    }
}
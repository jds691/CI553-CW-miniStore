package clients.customer;

import logic.LogicFactory;
import logic.RemoteLogicFactory;
import remote.access.Endpoint;

import javax.swing.*;

/**
 * The standalone Customer Client
 */
public class CustomerClient {
    public static void main(String[] args) {
        String productURL = args.length < 1
                ? Endpoint.PRODUCT_READ
                : args[0];

        String stockURL = args.length < 2
                ? Endpoint.STOCK_WRITE
                : args[1];

        String orderURL = args.length < 3
                ? Endpoint.ORDER
                : args[2];

        RemoteLogicFactory factory = new RemoteLogicFactory(orderURL, productURL, stockURL);
        displayGUI(factory);
    }

    private static void displayGUI(LogicFactory factory) {
        JFrame window = new JFrame();
        window.setTitle("Customer Client (MVC RMI)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CustomerModel model = new CustomerModel(factory);
        CustomerView view = new CustomerView(window, 0, 0);
        CustomerController controller = new CustomerController(model);
        view.setController(controller);

        window.setVisible(true);
    }
}

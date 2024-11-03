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
        String stockURL = args.length < 1         // URL of stock R
                        ? Endpoint.PRODUCT_READ           //  default  location
                        : args[0];                //  supplied location

        RemoteLogicFactory factory = new RemoteLogicFactory(null, null, stockURL);
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

        model.addObserver(view);
        window.setVisible(true);
    }
}

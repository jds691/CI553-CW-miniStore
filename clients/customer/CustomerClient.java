package clients.customer;

import middle.MiddleFactory;
import middle.Endpoint;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone Customer Client
 */
public class CustomerClient {
    public static void main(String[] args) {
        String stockURL = args.length < 1         // URL of stock R
                        ? Endpoint.STOCK_READ_ONLY           //  default  location
                        : args[0];                //  supplied location

        RemoteMiddleFactory factory = new RemoteMiddleFactory();
        factory.setStockRInfo(stockURL);
        displayGUI(factory);
    }

    private static void displayGUI(MiddleFactory factory) {
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

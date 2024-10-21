package clients.customer;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone Customer Client
 */
public class CustomerClient {
    public static void main(String[] args) {
        String stockURL = args.length < 1         // URL of stock R
                        ? Names.STOCK_R           //  default  location
                        : args[0];                //  supplied location

        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRInfo(stockURL);
        displayGUI(mrf);
    }

    private static void displayGUI(MiddleFactory mf) {
        JFrame window = new JFrame();
        window.setTitle("Customer Client (MVC RMI)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CustomerModel model = new CustomerModel(mf);
        CustomerView view = new CustomerView(window, mf, 0, 0);
        CustomerController cont = new CustomerController(model, view);
        view.setController(cont);

        model.addObserver(view);
        window.setVisible(true);
    }
}

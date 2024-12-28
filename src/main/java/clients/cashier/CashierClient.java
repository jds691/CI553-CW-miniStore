package clients.cashier;

import logic.LogicFactory;
import logic.RemoteLogicFactory;
import remote.access.Endpoint;

import javax.swing.*;

/**
 * The standalone Cashier Client.
 */
public class CashierClient {
    public static void main(String[] args) {
        String stockURL = args.length < 1     // URL of stock RW
                        ? Endpoint.STOCK_WRITE      //  default  location
                        : args[0];            //  supplied location
        String orderURL = args.length < 2     // URL of order
                        ? Endpoint.ORDER         //  default  location
                        : args[1];            //  supplied location

        RemoteLogicFactory factory = new RemoteLogicFactory(orderURL, null, stockURL);
        displayGUI(factory);
    }

    private static void displayGUI(LogicFactory factory) {
        JFrame window = new JFrame();

        window.setTitle("Cashier Client (MVC RMI)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CashierModel model = new CashierModel(factory);
        CashierView view = new CashierView(window, 0, 0);
        CashierController controller = new CashierController(model);
        view.setController(controller);

        window.setVisible(true);
        model.askForUpdate();
    }
}

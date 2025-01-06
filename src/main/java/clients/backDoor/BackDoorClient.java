package clients.backDoor;

import logic.LogicFactory;
import logic.RemoteLogicFactory;
import remote.access.Endpoint;

import javax.swing.*;

/**
 * The standalone BackDoor Client
 */
public class BackDoorClient {
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

        window.setTitle("BackDoor Client (MVC RMI)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BackDoorModel model = new BackDoorModel(factory);
        BackDoorView view = new BackDoorView(window, 0, 0);
        BackDoorController controller = new BackDoorController(model);
        view.setController(controller);

        model.addObserver(view);
        window.setVisible(true);
    }
}

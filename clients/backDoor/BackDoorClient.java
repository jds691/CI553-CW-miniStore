package clients.backDoor;

import middle.MiddleFactory;
import middle.Endpoint;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone BackDoor Client
 */
public class BackDoorClient {
    public static void main(String[] args) {
        String stockURL = args.length < 1     // URL of stock RW
                        ? Endpoint.STOCK_READ_WRITE      //  default  location
                        : args[0];            //  supplied location
        String orderURL = args.length < 2     // URL of order
                        ? Endpoint.ORDER         //  default  location
                        : args[1];            //  supplied location

        RemoteMiddleFactory factory = new RemoteMiddleFactory();
        factory.setStockRWInfo(stockURL);
        factory.setOrderInfo(orderURL);
        displayGUI(factory);
    }

    private static void displayGUI(MiddleFactory factory) {
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

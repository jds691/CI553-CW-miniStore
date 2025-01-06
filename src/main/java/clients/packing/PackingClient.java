package clients.packing;


import logic.LogicFactory;
import logic.RemoteLogicFactory;
import remote.access.Endpoint;

import javax.swing.*;

/**
 * The standalone warehouse Packing Client. warehouse staff to pack the bought order
 *
 * @author Mike Smith University of Brighton
 * @author Shine University of Brighton
 * @version year 2024
 */
public class PackingClient {
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

    public static void displayGUI(LogicFactory factory) {
        JFrame window = new JFrame();

        window.setTitle("Packing Client (RMI MVC)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PackingModel model = new PackingModel(factory);
        PackingController controller = new PackingController(model);
        PackingView view = new PackingView(window, controller,0, 0);

        model.addObserver(view);
        window.setVisible(true);
    }
}


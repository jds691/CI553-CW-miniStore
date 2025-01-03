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
        String stockURL = args.length < 1     // URL of stock RW
                        ? Endpoint.STOCK_WRITE      //  default  location
                        : args[0];            //  supplied location
        String orderURL = args.length < 2     // URL of order
                        ? Endpoint.ORDER         //  default  location
                        : args[1];            //  supplied location

        RemoteLogicFactory factory = new RemoteLogicFactory(orderURL, null, stockURL);
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


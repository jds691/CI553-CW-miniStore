package clients.packing;


import middle.MiddleFactory;
import middle.Endpoint;
import middle.RemoteMiddleFactory;

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

    public static void displayGUI(MiddleFactory factory) {
        JFrame window = new JFrame();

        window.setTitle("Packing Client (RMI MVC)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PackingModel model = new PackingModel(factory);
        PackingView view = new PackingView(window, 0, 0);
        PackingController controller = new PackingController(model);
        view.setController(controller);

        model.addObserver(view);
        window.setVisible(true);
    }
}


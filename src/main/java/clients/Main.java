package clients;

import clients.backDoor.BackDoorController;
import clients.backDoor.BackDoorModel;
import clients.backDoor.BackDoorView;
import clients.cashier.CashierController;
import clients.cashier.CashierModel;
import clients.cashier.CashierView;
import clients.customer.CustomerController;
import clients.customer.CustomerModel;
import clients.customer.CustomerView;
import clients.packing.PackingController;
import clients.packing.PackingModel;
import clients.packing.PackingView;
import logic.LocalLogicFactory;
import logic.LogicFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Starts all the clients (user interface)  as a single application.
 * Good for testing the system using a single application.
 *
 * @author Mike Smith University of Brighton
 * @author Shine University of Brighton
 * @version year-2024
 */
class Main {
    public static void main(String[] args) {
        LogicFactory factory = new LocalLogicFactory();  // Direct remote.access

        startCustomerGUI_MVC(factory);
        startCashierGUI_MVC(factory);
        startPackingGUI_MVC(factory);
        startBackDoorGUI_MVC(factory);
    }

    /**
     * start the Customer client, -search product
     *
     * @param factory A factory to create objects to remote.access the stock list
     */
    public static void startCustomerGUI_MVC(LogicFactory factory) {
        JFrame window = new JFrame();
        window.setTitle("Customer Client MVC");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension pos = PosOnScrn.getPos();

        CustomerModel model = new CustomerModel(factory);
        CustomerView view = new CustomerView(window, pos.width, pos.height);
        CustomerController controller = new CustomerController(model);
        view.setController(controller);

        window.setVisible(true);
    }

    /**
     * start the cashier client - customer check stock, buy product
     *
     * @param factory A factory to create objects to remote.access the stock list
     */
    public static void startCashierGUI_MVC(LogicFactory factory) {
        JFrame window = new JFrame();
        window.setTitle("Cashier Client MVC");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension pos = PosOnScrn.getPos();

        CashierModel model = new CashierModel(factory);
        CashierView view = new CashierView(window, pos.width, pos.height);
        CashierController cont = new CashierController(model);
        view.setController(cont);

        window.setVisible(true);
        // Initial display
        model.askForUpdate();
    }

    /**
     * start the Packing client - for warehouse staff to pack the bought order for customer, one order at a time
     *
     * @param factory A factory to create objects to remote.access the stock list
     */
    public static void startPackingGUI_MVC(LogicFactory factory) {
        JFrame window = new JFrame();

        window.setTitle("Packing Client MVC");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension pos = PosOnScrn.getPos();

        PackingModel model = new PackingModel(factory);
        PackingController controller = new PackingController(model);
        PackingView view = new PackingView(window, controller, pos.width, pos.height);

        model.addObserver(view);
        window.setVisible(true);
    }

    /**
     * start the BackDoor client - store staff to check and update stock
     *
     * @param factory A factory to create objects to remote.access the stock list
     */
    public static void startBackDoorGUI_MVC(LogicFactory factory) {
        JFrame window = new JFrame();

        window.setTitle("BackDoor Client MVC");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension pos = PosOnScrn.getPos();

        BackDoorModel model = new BackDoorModel(factory);
        BackDoorView view = new BackDoorView(window, pos.width, pos.height);
        BackDoorController controller = new BackDoorController(model);
        view.setController(controller);

        window.setVisible(true);
    }
}

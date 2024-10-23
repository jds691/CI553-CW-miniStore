package clients.packing;

import catalogue.Basket;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Packing view.
 */

public class PackingView implements Observer {
    private static final String PACKED = "Packed";

    private static final int HEIGHT = 300;
    private static final int WIDTH = 400;

    private final JLabel pageTitle = new JLabel();
    private final JLabel promptLabel = new JLabel();
    private final JTextArea messageOutput = new JTextArea();
    private final JScrollPane messageScrollPane = new JScrollPane();
    private final JButton packButton = new JButton(PACKED);

    private PackingController controller = null;

    /**
     * Construct the view
     *
     * @param rootPane Window in which to construct
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public PackingView(RootPaneContainer rootPane, int x, int y) {
        // Content Pane
        Container contentPane = rootPane.getContentPane();
        Container rootWindow = (Container) rootPane;
        contentPane.setLayout(null);
        rootWindow.setSize(WIDTH, HEIGHT);
        rootWindow.setLocation(x, y);

        Font monospaceFont = new Font("Monospaced", Font.PLAIN, 12);

        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Packing Bought Order");
        contentPane.add(pageTitle);

        packButton.setBounds(16, 25, 80, 40);
        packButton.addActionListener(
                e -> controller.packOrder()
        );
        contentPane.add(packButton);

        promptLabel.setBounds(110, 25, 270, 20);
        promptLabel.setText("");
        contentPane.add(promptLabel);

        messageScrollPane.setBounds(110, 55, 270, 205);
        messageOutput.setText("");
        messageOutput.setFont(monospaceFont);
        contentPane.add(messageScrollPane);

        messageScrollPane.getViewport().add(messageOutput);
        rootWindow.setVisible(true);
    }

    public void setController(PackingController controller) {
        this.controller = controller;
    }

    /**
     * Update the view
     *
     * @param modelC The observed model
     * @param arg    Specific args
     */
    @Override
    public void update(Observable modelC, Object arg) {
        PackingModel model = (PackingModel) modelC;
        String message = (String) arg;
        promptLabel.setText(message);

        Basket basket = model.getBasket();
        if (basket != null) {
            messageOutput.setText(basket.getDetails());
        } else {
            messageOutput.setText("");
        }
    }
}


package clients.customer;

import clients.Picture;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */

public class CustomerView implements Observer {
    // Names of buttons
    class Name {
        public static final String CHECK = "Check";
        public static final String CLEAR = "Clear";
    }

    private static final int HEIGHT = 300;
    private static final int WIDTH = 400;

    private final JLabel pageTitle = new JLabel();
    private final JLabel promptLabel = new JLabel();
    private final JTextField productNumberInput = new JTextField();
    private final JTextArea messageOutput = new JTextArea();
    private final JScrollPane messageScrollPane = new JScrollPane();
    private final JButton checkButton = new JButton(Name.CHECK);
    private final JButton clearButton = new JButton(Name.CLEAR);

    private final Picture productPicture = new Picture(80, 80);
    private CustomerController controller = null;

    /**
     * Construct the view
     *
     * @param rootPane Window in which to construct
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public CustomerView(RootPaneContainer rootPane, int x, int y) {
        Container contentPane = rootPane.getContentPane();
        Container rootWindow = (Container) rootPane;
        contentPane.setLayout(null);
        rootWindow.setSize(WIDTH, HEIGHT);
        rootWindow.setLocation(x, y);

        Font monospaceFont = new Font("Monospaced", Font.PLAIN, 12);

        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Search products");
        contentPane.add(pageTitle);

        checkButton.setBounds(16, 25, 80, 40);
        checkButton.addActionListener(
                e -> controller.doCheck(productNumberInput.getText())
        );
        contentPane.add(checkButton);

        clearButton.setBounds(16, 25 + 60, 80, 40);
        clearButton.addActionListener(
                e -> controller.reset()
        );
        contentPane.add(clearButton);

        promptLabel.setBounds(110, 25, 270, 20);
        promptLabel.setText("");
        contentPane.add(promptLabel);

        productNumberInput.setBounds(110, 50, 270, 40);
        productNumberInput.setText("");
        contentPane.add(productNumberInput);

        messageScrollPane.setBounds(110, 100, 270, 160);
        messageOutput.setText("");
        messageOutput.setFont(monospaceFont);
        contentPane.add(messageScrollPane);
        messageScrollPane.getViewport().add(messageOutput);

        // Picture area
        productPicture.setBounds(16, 25 + 60 * 2, 80, 80);
        contentPane.add(productPicture);
        productPicture.clear();

        rootWindow.setVisible(true);
        productNumberInput.requestFocus();
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     *
     * @param controller The controller
     */
    public void setController(CustomerController controller) {
        this.controller = controller;
    }

    /**
     * Update the view
     *
     * @param modelC The observed model
     * @param arg    Specific args
     */
    public void update(Observable modelC, Object arg) {
        CustomerModel model = (CustomerModel) modelC;
        String message = (String) arg;
        promptLabel.setText(message);
        ImageIcon image = model.getPicture();

        if (image == null) {
            productPicture.clear();
        } else {
            productPicture.set(image);
        }

        //TODO: Add back rich message output
        //messageOutput.setText(model.getProducts().getRichDescription());
        productNumberInput.requestFocus();
    }
}

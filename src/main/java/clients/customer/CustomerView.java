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
    private final Picture productPicture = new Picture(120, 120);
    private final JLabel productNameLabel = new JLabel();
    private final JLabel productMetadataLabel = new JLabel();
    private final JLabel productDescriptionLabel = new JLabel();
    private final JLabel productNumberPromptLabel = new JLabel();
    private final JTextField productNumberInput = new JTextField();
    private final JButton searchButton = new JButton("Search");


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

        Font pageTitleFont = new Font("Dialog", Font.BOLD, 18);
        pageTitle.setBounds(16, 16, 368, 20);
        pageTitle.setFont(pageTitleFont);
        pageTitle.setText("Product Search");
        contentPane.add(pageTitle);

        productPicture.setBounds(16, 56, 120, 120);
        contentPane.add(productPicture);
        productPicture.clear();

        Font productNameFont = new Font("Dialog", Font.BOLD, 14);
        productNameLabel.setBounds(152, 56, 232, 16);
        productNameLabel.setFont(productNameFont);
        contentPane.add(productNameLabel);

        Font productMetadataFont = new Font("Dialog", Font.PLAIN, 10);
        productMetadataLabel.setBounds(152, 72, 232, 12);
        productMetadataLabel.setFont(productMetadataFont);
        contentPane.add(productMetadataLabel);

        productDescriptionLabel.setBounds(152, 89, 232, 87);
        contentPane.add(productDescriptionLabel);

        searchButton.setBounds(304, 214, 80, 40);
        searchButton.addActionListener(
                e -> controller.doCheck(productNumberInput.getText())
        );
        contentPane.add(searchButton);

        productNumberPromptLabel.setBounds(16, 195, 270, 20);
        productNumberPromptLabel.setText("Product Name/ID");
        contentPane.add(productNumberPromptLabel);

        productNumberInput.setBounds(16, 214, 270, 40);
        productNumberInput.setText("");
        contentPane.add(productNumberInput);

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
        //promptLabel.setText(message);
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

package clients.customer;

import clients.Picture;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static clients.customer.CustomerModel.Property;

/**
 * Implements the Customer view.
 */

public class CustomerView implements PropertyChangeListener {
    private static final int HEIGHT = 300;
    private static final int WIDTH = 400;

    private final JLabel pageTitle = new JLabel();
    private final Picture productPicture = new Picture(120, 120);
    private final JLabel productNameLabel = new JLabel();
    private final JLabel productMetadataLabel = new JLabel();
    private final JTextArea productDescriptionLabel = new JTextArea();
    private final JLabel productNumberPromptLabel = new JLabel();
    private final JTextField productNumberInput = new JTextField();
    private final JButton searchButton = new JButton("Search");

    private final JLabel promptLabel = new JLabel();


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
        productDescriptionLabel.setWrapStyleWord(true);
        productDescriptionLabel.setLineWrap(true);
        productDescriptionLabel.setEditable(false);
        productDescriptionLabel.setOpaque(false);
        productDescriptionLabel.setFocusable(false);
        contentPane.add(productDescriptionLabel);

        searchButton.setBounds(304, 214, 80, 40);
        searchButton.addActionListener(
                e -> controller.queryProduct(productNumberInput.getText())
        );
        contentPane.add(searchButton);

        productNumberPromptLabel.setBounds(16, 195, 270, 20);
        productNumberPromptLabel.setText("Product Name/ID");
        contentPane.add(productNumberPromptLabel);

        productNumberInput.setBounds(16, 214, 270, 40);
        productNumberInput.setText("");
        contentPane.add(productNumberInput);

        promptLabel.setBounds(16, 56, 368, 120);
        promptLabel.setHorizontalAlignment(SwingConstants.CENTER);
        promptLabel.setVerticalAlignment(SwingConstants.CENTER);
        promptLabel.setFont(pageTitleFont);
        contentPane.add(promptLabel);

        setProductDetailsVisible(false);
        setPromptLabel("Search Product");

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
        this.controller.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        /*CustomerModel model = (CustomerModel) modelC;
        String message = (String) arg;
        //promptLabel.setText(message);
        ImageIcon image = model.getProductImage();

        if (image == null) {
            productPicture.clear();
        } else {
            productPicture.set(image);
        }

        //TODO: Add back rich message output
        //messageOutput.setText(model.getProducts().getRichDescription());
        productNumberInput.requestFocus();*/

        String property = evt.getPropertyName();

        switch (property) {
            case Property.PROMPT:
                setPromptLabel((String) evt.getNewValue());
                break;
            case Property.SELECTED_PRODUCT:
                Object value = evt.getNewValue();

                if (value != null) {
                    productNameLabel.setText(controller.getProductName());
                    productMetadataLabel.setText(controller.getProductMetadata());
                    productDescriptionLabel.setText(controller.getProductDescription());
                    productPicture.set(controller.getProductImage());
                    setProductDetailsVisible(true);
                } else {
                    setProductDetailsVisible(false);
                }
                break;
            default:
                break;
            //throw new IllegalStateException("Unexpected value: " + property);
        }
    }

    private void setProductDetailsVisible(boolean visible) {
        productPicture.setVisible(visible);
        productNameLabel.setVisible(visible);
        productMetadataLabel.setVisible(visible);
        productDescriptionLabel.setVisible(visible);
    }

    private void setPromptLabel(String text) {
        if (text.isBlank()) {
            promptLabel.setVisible(false);
            return;
        }

        promptLabel.setText(text);
        promptLabel.setVisible(true);
    }
}

package clients.backDoor;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */
public class BackDoorView implements Observer {
    private static final String RESTOCK = "Add";
    private static final String CLEAR = "Clear";
    private static final String QUERY = "Query";

    private static final int HEIGHT = 300;
    private static final int WIDTH = 400;

    private final JLabel pageTitle = new JLabel();
    private final JLabel promptLabel = new JLabel();
    private final JTextField productNumberInput = new JTextField();
    private final JTextField quantityInput = new JTextField();
    private final JTextArea messageOutput = new JTextArea();
    private final JScrollPane messageScrollPane = new JScrollPane();
    private final JButton clearButton = new JButton(CLEAR);
    private final JButton restockButton = new JButton(RESTOCK);
    private final JButton queryButton = new JButton(QUERY);

    private BackDoorController controller = null;

    /**
     * Construct the view
     *
     * @param rootPane Window in which to construct
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public BackDoorView(RootPaneContainer rootPane, int x, int y) {
        Container contentPane = rootPane.getContentPane();
        Container rootWindow = (Container) rootPane;
        // No layout manager
        contentPane.setLayout(null);
        rootWindow.setSize(WIDTH, HEIGHT);
        rootWindow.setLocation(x, y);

        Font monospaceFont = new Font("Monospaced", Font.PLAIN, 12);

        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Staff check and manage stock");
        contentPane.add(pageTitle);

        queryButton.setBounds(16, 25, 80, 40);
        queryButton.addActionListener(
                e -> controller.queryProduct(productNumberInput.getText())
        );
        contentPane.add(queryButton);

        restockButton.setBounds(16, 25 + 60, 80, 40);
        restockButton.addActionListener(e -> {
            try {
                controller.restockProduct(productNumberInput.getText(), quantityInput.getText());
            } catch (NumberFormatException exception) {
                promptLabel.setText(exception.getMessage());
            }
        });
        contentPane.add(restockButton);

        clearButton.setBounds(16, 25 + 60 * 2, 80, 40);
        clearButton.addActionListener(
                e -> controller.reset()
        );
        contentPane.add(clearButton);

        promptLabel.setBounds(110, 25, 270, 20);
        promptLabel.setText("");
        contentPane.add(promptLabel);

        // Input Area
        productNumberInput.setBounds(110, 50, 120, 40);
        productNumberInput.setText("");
        contentPane.add(productNumberInput);

        // Input Area
        quantityInput.setBounds(260, 50, 120, 40);
        quantityInput.setText("0");
        contentPane.add(quantityInput);

        // Scrolling pane
        messageScrollPane.setBounds(110, 100, 270, 160);
        messageOutput.setText("");
        messageOutput.setFont(monospaceFont);
        contentPane.add(messageScrollPane);

        messageScrollPane.getViewport().add(messageOutput);
        rootWindow.setVisible(true);
        productNumberInput.requestFocus();
    }

    public void setController(BackDoorController controller) {
        this.controller = controller;
    }

    /**
     * Update the view, called by notifyObservers(theAction) in model,
     *
     * @param modelC The observed model
     * @param arg    Specific args
     */
    @Override
    public void update(Observable modelC, Object arg) {
        BackDoorModel model = (BackDoorModel) modelC;
        String message = (String) arg;
        promptLabel.setText(message);

        //TODO: Reimplement message output
        //messageOutput.setText(model.getProducts().getRichDescription());
        productNumberInput.requestFocus();
    }
}
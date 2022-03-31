package ui;

import data.Order;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * creates orderDialog
 */
public class OrderDialog extends JDialog {
    private Order order;
    SpringLayout layout = new SpringLayout();

    JPanel panel;
    JLabel nameOfStockLabel;
    JLabel quantityOfStocksLabel;
    JLabel priceOfOneStockLabel;
    JTextField costTextField;
    JTextField nameTextField;
    JTextField quantityTextField;
    JTextField priceTextField;
    JLabel purchaseCostLabel;
    JButton sendButton;
    JButton cancelButton;

    public OrderDialog(Frame owner, String title, boolean modal, Order order) {
        super(owner, title, modal);
        this.order = order;

        setSize(400, 250);

        panel = new JPanel();
        panel.setLayout(layout);

        nameOfStockLabel = new JLabel("Enter name of stock:");
        panel.add(nameOfStockLabel);

        nameTextField = new JTextField();
        nameTextField.setDocument(new JTextFieldLimit(4));
        nameTextField.setPreferredSize(new Dimension(200, 20));
        if (order.getName() != null) {
            nameTextField.setText(order.getName());
        }
        panel.add(nameTextField);

        quantityOfStocksLabel = new JLabel("Enter quantity of stocks:");
        panel.add(quantityOfStocksLabel);

        quantityTextField = new JTextField();
        quantityTextField.setPreferredSize(new Dimension(200, 20));
        PlainDocument quantityDocument = (PlainDocument) quantityTextField.getDocument();
        quantityDocument.setDocumentFilter(new DigitFilter());
        panel.add(quantityTextField);

        priceOfOneStockLabel = new JLabel("Enter price of one stock");
        panel.add(priceOfOneStockLabel);

        priceTextField = new JTextField();
        priceTextField.setPreferredSize(new Dimension(200, 20));
        PlainDocument priceDocument = (PlainDocument) priceTextField.getDocument();
        priceDocument.setDocumentFilter(new DigitFilter());
        priceTextField.setText(String.valueOf(order.getAskPrice()));
        panel.add(priceTextField);

        purchaseCostLabel = new JLabel("Purchase cost");
        panel.add(purchaseCostLabel);

        costTextField = new JTextField();
        costTextField.setEditable(false);
        costTextField.setPreferredSize(new Dimension(200, 20));
        panel.add(costTextField);

        quantityTextField.addKeyListener(new PriceKeyListener(quantityTextField, priceTextField, costTextField));
        priceTextField.addKeyListener(new PriceKeyListener(quantityTextField, priceTextField, costTextField));

        sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendActionListener(nameTextField.getText(),
            Integer.parseInt(priceTextField.getText()), Integer.parseInt(quantityTextField.getText())));
        panel.add(sendButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        panel.add(cancelButton);

        makeLayout();

        this.add(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void sendActionListener(String name, int price, int quantity) {
        this.order = new Order(order.getAccountId(), name, quantity, price, LocalDateTime.now());
        this.dispose();
    }

    public Order getOrder() {
        return order;
    }

    private void makeLayout() {
        layout.putConstraint(SpringLayout.WEST, nameOfStockLabel, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, nameOfStockLabel, 10, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, nameTextField, 15, SpringLayout.EAST, nameOfStockLabel);
        layout.putConstraint(SpringLayout.NORTH, nameTextField, 10, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, quantityOfStocksLabel, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, quantityOfStocksLabel, 40, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.NORTH, quantityTextField, 40, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, quantityTextField, 15, SpringLayout.EAST, quantityOfStocksLabel);
        layout.putConstraint(SpringLayout.NORTH, priceOfOneStockLabel, 70, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, priceOfOneStockLabel, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.WEST, priceTextField, 15, SpringLayout.EAST, priceOfOneStockLabel);
        layout.putConstraint(SpringLayout.NORTH, priceTextField, 70, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.NORTH, purchaseCostLabel, 100, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, purchaseCostLabel, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.WEST, costTextField, 15, SpringLayout.EAST, purchaseCostLabel);
        layout.putConstraint(SpringLayout.NORTH, costTextField, 100, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, cancelButton, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, cancelButton, 20, SpringLayout.SOUTH, purchaseCostLabel);
        layout.putConstraint(SpringLayout.NORTH, sendButton, 20, SpringLayout.SOUTH, purchaseCostLabel);
        layout.putConstraint(SpringLayout.WEST, sendButton, 10, SpringLayout.EAST, cancelButton);
    }
}
package ui;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Special Listener for TextFields "price" and "quantity", that counts purchase cost and puts the result in
 * TextField "cost"
 */
public class PriceKeyListener implements KeyListener {
    private JTextField quantity;
    private JTextField price;
    private JTextField cost;

    public PriceKeyListener(JTextField quantity, JTextField price, JTextField cost) {
        this.quantity = quantity;
        this.price = price;
        this.cost = cost;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        try {
            if (quantity.getText().equals("") | price.getText().equals("")) {
                cost.setText("");
            } else {
                long quantityValue = Integer.parseInt(quantity.getText());
                long priceValue = Integer.parseInt(price.getText());
                long result = quantityValue * priceValue;
                cost.setText(String.valueOf(result));
            }
        } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            if (quantity.getText().equals("") | price.getText().equals("")) {
                cost.setText("");
            } else {
                long quantityValue = Integer.parseInt(quantity.getText());
                long priceValue = Integer.parseInt(price.getText());
                long result = quantityValue * priceValue;
                cost.setText(String.valueOf(result));
            }
        } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            if (quantity.getText().equals("") | price.getText().equals("")) {
                cost.setText("");
            } else {
                int quantityValue = Integer.parseInt(quantity.getText());
                int priceValue = Integer.parseInt(price.getText());
                cost.setText(String.valueOf(quantityValue * priceValue));
            }
        } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
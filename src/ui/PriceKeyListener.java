package ui;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Special Listener for TextFields "price" and "quantity", that counts purchase cost and puts the result in
 * TextField "cost"
 */
public class PriceKeyListener extends KeyAdapter {
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
        keyActions();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyActions();
    }

    public void keyActions() {
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
}
package ui;

import data.Account;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * creates accountDialog
 */
public class AccountDialog extends JDialog{
    Account account;

    public AccountDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(250, 100);

        JPanel panel = new JPanel();
        JPanel withText = new JPanel();

        JTextArea accName = new JTextArea();
        accName.setPreferredSize(new Dimension(200, 20));

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());

        JButton save = new JButton("Save");
        save.addActionListener(e -> saveActionListener(accName));
        withText.add(accName);
        panel.add(cancel);
        panel.add(save);
        this.add(withText, BorderLayout.NORTH);
        this.add(panel, BorderLayout.SOUTH);
    }

    public void saveActionListener(JTextArea name) {
        this.account = new Account(name.getText(), LocalDateTime.now());
        this.dispose();
    }

    public Account getAccount() {
        return account;
    }
}
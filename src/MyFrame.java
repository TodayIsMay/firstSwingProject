import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MyFrame extends JFrame {
    private DefaultTableModel dtm;
    List<Account> accounts = new ArrayList<>();
    Generator generator = new Generator();
    JTable table;
    JScrollPane scrollPane;
    private Object[] columHeader = new String[]{"id", "Stock name", "Stock quantity", "Ask price"};
    JComboBox<String> comboBox;
    Container container;
    SpringLayout layout;

    public MyFrame() {
        dtm = new DefaultTableModel();
        dtm.setColumnIdentifiers(columHeader);
        container = getContentPane();
        layout = new SpringLayout();
        container.setLayout(layout);
        JButton bid = new JButton("Add new bid");
        bid.addActionListener(e -> {
            JDialog dialog = createBidDialog("Create new bid", true);
            dialog.setVisible(true);
        });
        container.add(bid);
        layout.putConstraint(SpringLayout.SOUTH, bid, -20, SpringLayout.SOUTH, container);
        layout.putConstraint(SpringLayout.WEST, bid, 150, SpringLayout.WEST, container);
        comboBox = new JComboBox<>();
        comboBox.setPreferredSize(new Dimension(250, 20));
        container.add(comboBox);
        layout.putConstraint(SpringLayout.WEST, comboBox, 10, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, comboBox, 13, SpringLayout.NORTH, container);
        table = new JTable(dtm);
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(410, 340));
        container.add(scrollPane);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 45, SpringLayout.NORTH, container);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = (String) comboBox.getSelectedItem();
                Account acc = null;
                for (Account account : accounts) {
                    if (account.getId() == Integer.parseInt(id)) {
                        acc = account;
                        break;
                    }
                }
                if(acc != null) {
                    updateTable(acc);
                }
            }
        });
        JButton button = new JButton("Add new account");
        button.addActionListener(e -> {
            JDialog dialog = createAccountDialog("Add new account", true);
            dialog.setVisible(true);
        });
        container.add(button);
        layout.putConstraint(SpringLayout.WEST, button, 10, SpringLayout.EAST, comboBox);
        layout.putConstraint(SpringLayout.NORTH, button, 10, SpringLayout.NORTH, container);
        setTitle("FirstSwingTask");
        setPreferredSize(new Dimension(430, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private JDialog createAccountDialog(String title, boolean modal) {
        JDialog dialog = new JDialog(this, title, modal);
        JPanel panel = new JPanel();
        JPanel withText = new JPanel();
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(250, 100);
        JTextArea accName = new JTextArea();
        accName.setPreferredSize(new Dimension(200, 20));
        JButton cancel = new JButton("Cancel");
        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account account = new Account(generator.generate(), accName.getText());
                accounts.add(account);
                comboBox.addItem(account.toString());
                dialog.dispose();
            }
        });
        withText.add(accName);
        panel.add(cancel);
        panel.add(save);
        dialog.add(withText, BorderLayout.NORTH);
        dialog.add(panel, BorderLayout.SOUTH);
        return dialog;
    }

    public JDialog createBidDialog(String title, boolean modal) {
        JDialog dialog = new JDialog(this, title, modal);
        dialog.setSize(400, 250);
        JPanel panel = new JPanel();
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);
        JLabel nameOfStock = new JLabel("Enter name of stock:");
        panel.add(nameOfStock);
        JTextField name = new JTextField();
        name.setPreferredSize(new Dimension(200, 20));
        panel.add(name);
        JLabel quantityOfStocks = new JLabel("Enter quantity of stocks:");
        panel.add(quantityOfStocks);
        JTextField quantity = new JTextField();
        quantity.setPreferredSize(new Dimension(200, 20));
        panel.add(quantity);
        JLabel priceOfOneStock = new JLabel("Enter price of one stock");
        panel.add(priceOfOneStock);
        JTextField price = new JTextField();
        price.setPreferredSize(new Dimension(200, 20));
        panel.add(price);
        JLabel purchaseCost = new JLabel("Purchase cost");
        panel.add(purchaseCost);
        JTextField cost = new JTextField();
        cost.setEditable(false);
        cost.setPreferredSize(new Dimension(200, 20));
        panel.add(cost);
        JButton send = new JButton("Send");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedItem() != null) {
                    Account account = null;
                    for (Account acc : accounts) {
                        if (acc.getId() == Integer.parseInt((String) comboBox.getSelectedItem())) {
                            account = acc;
                            break;
                        }
                    }
                    if (account != null) {
                        String stockName = name.getText();
                        int stockQuantity = Integer.parseInt(quantity.getText());
                        int stockPrice = Integer.parseInt(price.getText());
                        account.addBid(account.getId(), stockName, stockQuantity, stockPrice);
                        updateTable(account);
                    }
                }
                dialog.dispose();
            }
        });
        panel.add(send);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dialog.dispose());
        panel.add(cancel);
        layout.putConstraint(SpringLayout.WEST, nameOfStock, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, nameOfStock, 10, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, name, 15, SpringLayout.EAST, nameOfStock);
        layout.putConstraint(SpringLayout.NORTH, name, 10, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, quantityOfStocks, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, quantityOfStocks, 40, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.NORTH, quantity, 40, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, quantity, 15, SpringLayout.EAST, quantityOfStocks);
        layout.putConstraint(SpringLayout.NORTH, priceOfOneStock, 70, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, priceOfOneStock, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.WEST, price, 15, SpringLayout.EAST, priceOfOneStock);
        layout.putConstraint(SpringLayout.NORTH, price, 70, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.NORTH, purchaseCost, 100, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, purchaseCost, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.WEST, cost, 15, SpringLayout.EAST, purchaseCost);
        layout.putConstraint(SpringLayout.NORTH, cost, 100, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, cancel, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, cancel, 20, SpringLayout.SOUTH, purchaseCost);
        layout.putConstraint(SpringLayout.NORTH, send, 20, SpringLayout.SOUTH, purchaseCost);
        layout.putConstraint(SpringLayout.WEST, send, 10, SpringLayout.EAST, cancel);

        dialog.add(panel);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        return dialog;
    }

    public void updateTable(Account account) {
        List<Bid> bids = account.getBids();
        if (!bids.isEmpty()) {
            for (int i = 0; i < 100; i++) {
                try {
                    dtm.removeRow(0);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.out.println("там и так пусто");
                    break;
                }
            }
            for (int i = 0; i < bids.size(); i++) {
                dtm.addRow(new String[]{String.valueOf(bids.get(i).getId()), bids.get(i).getStockName(),
                        String.valueOf(bids.get(i).getQuantity()), String.valueOf(bids.get(i).getAskPrice())});
            }
        } else {
            for (int i = 0; i < 100; i++) {
                try {
                    dtm.removeRow(0);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.out.println("ну пусто же");
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new MyFrame();
    }
}
package main;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import entities.Account;
import entities.Order;
import utilities.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MyFrame extends JFrame {
    DataBaseConnector connector = new DataBaseConnector();
    Connection connection;
    private final DefaultTableModel dtm;
    private final List<Account> accounts = new ArrayList<>();
    private final JComboBox<String> comboBox;

    public MyFrame() {
        connector.connect();
        connection = connector.getConnection();
        Queries queries = new Queries(connection);
        dtm = new DefaultTableModel();
        Object[] columHeader = new String[]{"id", "Stock name", "Stock quantity", "Ask price", "Cancel order"};
        dtm.setColumnIdentifiers(columHeader);
        Container container = getContentPane();
        SpringLayout layout = new SpringLayout();
        container.setLayout(layout);
        JButton order = new JButton("Add new order");
        order.addActionListener(e -> {
            JDialog dialog = createOrderDialog("Create new order", true, queries);
            dialog.setVisible(true);
        });
        container.add(order);
        layout.putConstraint(SpringLayout.SOUTH, order, -20, SpringLayout.SOUTH, container);
        layout.putConstraint(SpringLayout.WEST, order, 150, SpringLayout.WEST, container);
        comboBox = new JComboBox<>();
        comboBox.setPreferredSize(new Dimension(250, 20));
        container.add(comboBox);
        layout.putConstraint(SpringLayout.WEST, comboBox, 10, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, comboBox, 13, SpringLayout.NORTH, container);
        JTable table = new JTable(dtm);
        table.getColumn("Cancel order").setCellRenderer(new ButtonRenderer());
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                    row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                return c;
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JTable target = (JTable) e.getSource();
                int row = target.getSelectedRow();
                int id = Integer.parseInt((String) dtm.getValueAt(row, 0));
                dtm.removeRow(row);
                queries.deleteOrder(id);
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(410, 340));
        container.add(scrollPane);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 45, SpringLayout.NORTH, container);
        try {
            for (Account newAccount : queries.getAccountsFromDb()) {
                accounts.add(newAccount);
                comboBox.addItem(String.valueOf(newAccount.getId()));
                System.out.println("account_id: " + newAccount.getId());
            }
            for (Account acc : accounts) {
                queries.getOrdersFromAccount(acc);
            }
        } catch (NullPointerException exception) {
            System.out.println("ошибка при заполнении комбобокса при запуске программы");
            System.out.println(exception.getMessage());
        }
        if (!accounts.isEmpty()) {
            String id = (String) comboBox.getSelectedItem();
            Account acc = null;
            for (Account account : accounts) {
                if (account.getId() == Integer.parseInt(id)) {
                    acc = account;
                    break;
                }
            }
            if (acc != null)
                updateTable(acc, queries);
        }

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
                if (acc != null)
                    updateTable(acc, queries);
            }
        });
        JButton button = new JButton("Add new account");
        button.addActionListener(e -> {
            JDialog dialog = createAccountDialog("Add new account", true, queries);
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

    private JDialog createAccountDialog(String title, boolean modal, Queries queries) {
        JDialog dialog = new JDialog(this, title, modal);
        JPanel panel = new JPanel();
        JPanel withText = new JPanel();
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(250, 100);
        JTextArea accName = new JTextArea();
        accName.setPreferredSize(new Dimension(200, 20));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dialog.dispose());
        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accNameString = accName.getText();
                LocalDateTime creationTime = LocalDateTime.now();
                if (!accNameString.equals("")) {
                    try {
                        queries.insert(accNameString, creationTime);
                    } catch (NullPointerException exception) {
                        System.out.println(exception.getMessage());
                        System.out.println("Ошибка создания стэйтмента при создании аккаунта");
                    }
                    Account account = queries.getId(accNameString, creationTime);
                    accounts.add(account);
                    comboBox.addItem(String.valueOf(account.getId()));
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Name of the account shouldn't be empty!");
                }
            }
        });
        withText.add(accName);
        panel.add(cancel);
        panel.add(save);
        dialog.add(withText, BorderLayout.NORTH);
        dialog.add(panel, BorderLayout.SOUTH);
        return dialog;
    }

    public JDialog createOrderDialog(String title, boolean modal, Queries queries) {
        JDialog dialog = new JDialog(this, title, modal);
        dialog.setSize(400, 250);
        JPanel panel = new JPanel();
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);
        JLabel nameOfStock = new JLabel("Enter name of stock:");
        panel.add(nameOfStock);
        JTextField name = new JTextField();
        name.setDocument(new JTextFieldLimit(4));
        name.setPreferredSize(new Dimension(200, 20));
        panel.add(name);
        JLabel quantityOfStocks = new JLabel("Enter quantity of stocks:");
        panel.add(quantityOfStocks);
        JTextField quantity = new JTextField();
        quantity.setPreferredSize(new Dimension(200, 20));
        PlainDocument quantityDocument = (PlainDocument) quantity.getDocument();
        quantityDocument.setDocumentFilter(new DigitFilter());
        panel.add(quantity);
        JLabel priceOfOneStock = new JLabel("Enter price of one stock");
        panel.add(priceOfOneStock);
        JTextField price = new JTextField();
        price.setPreferredSize(new Dimension(200, 20));
        PlainDocument priceDocument = (PlainDocument) price.getDocument();
        priceDocument.setDocumentFilter(new DigitFilter());
        panel.add(price);
        JLabel purchaseCost = new JLabel("Purchase cost");
        panel.add(purchaseCost);
        JTextField cost = new JTextField();
        cost.setEditable(false);
        cost.setPreferredSize(new Dimension(200, 20));
        panel.add(cost);
        quantity.addKeyListener(new PriceKeyListener(quantity, price, cost));
        price.addKeyListener(new PriceKeyListener(quantity, price, cost));
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
                        LocalDateTime creationTime = LocalDateTime.now();
                        String stockName = name.getText();
                        int stockQuantity = Integer.parseInt(quantity.getText());
                        int stockPrice = Integer.parseInt(price.getText());
                        try {
                            queries.insertOrder(account, stockName, stockQuantity, stockPrice, creationTime);
                            queries.addOrder(account, stockName, stockQuantity, stockPrice, creationTime);
                            updateTable(account, queries);
                        } catch (NullPointerException exception) {
                            System.out.println("ошибос в создании ордера");
                            System.out.println(exception.getMessage());
                        }
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

    public void updateTable(Account account, Queries queries) {
        List<Order> orders = account.getOrders();
        if (!orders.isEmpty()) {
            for (int i = 0; i < 100; i++) {
                try {
                    dtm.removeRow(0);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    break;
                }
            }
            try {
                for (String[] array : queries.getOrders(account)) {
                    dtm.addRow(array);
                }
            } catch (NullPointerException exception) {
                System.out.println("ошибос в updateTable");
                System.out.println(exception.getMessage());
            }
        } else {
            for (int i = 0; i < 100; i++) {
                try {
                    dtm.removeRow(0);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new MyFrame();
    }
}
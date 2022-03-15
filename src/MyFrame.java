import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MyFrame extends JFrame {
    List<Account> accounts = new ArrayList<>();
    Generator generator = new Generator();
    JTable table;
    private Object[][] array = new String[4][20];
    private Object[] columHeader = new String[]{"id", "Stock name", "Stock quantity", "Ask price"};
    JComboBox<String> comboBox;

    public MyFrame() {
        JButton bid = new JButton("Add new bid");
        bid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = createBidDialog("Create new bid", true);
                dialog.setVisible(true);
            }
        });
        JPanel bidPanel = new JPanel();
        bidPanel.add(bid);
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(new Label("Label!"), BorderLayout.NORTH);
        JPanel panel = new JPanel();
        comboBox = new JComboBox<>();
        comboBox.setPreferredSize(new Dimension(300, 30));
        panel.add(comboBox);
        container.add(panel, BorderLayout.NORTH);
        container.add(bidPanel, BorderLayout.SOUTH);
        JButton button = new JButton("Add new account");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = createAccountDialog("Add new account", true);
                dialog.setVisible(true);
            }
        });
        panel.add(button);
        table = new JTable(array, columHeader);
        container.add(table, BorderLayout.CENTER);
        container.add(panel, BorderLayout.NORTH);
        container.add(new JScrollPane(table));
        setTitle("FirstSwingTask");
        setPreferredSize(new Dimension(500, 480));
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
        panel.add(send);
        JButton cancel = new JButton("Cancel");
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

    public static void main(String[] args) {
        new MyFrame();
    }
}
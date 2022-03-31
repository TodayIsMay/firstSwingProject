package ui;

import data.*;
import db.DBSync;
import db.DataBaseConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;

/**
 * The main class of the program.
 * The program has the following features:
 * - create new account by the button "Add new account"
 * - create new order by the button "Add new order"
 * - create new order with prepared price and symbol name by pressing the cell in the column "Ask" in the "symbolTable"
 */
public class MyFrame extends JFrame implements UpdateListener, ModelListener {
    private final int ORDER_ID_COLUMN_NUMBER = 0;
    private final String CANCEL_ORDER_COLUMN_HEADER = "Cancel order";
    private final int CANCEL_ORDER_COLUMN_NUMBER = 4;

    DataBaseConnector connector = new DataBaseConnector();
    Connection connection;
    private final DefaultTableModel orderDtm;
    private final DefaultTableModel symbolDtm;
    private final JComboBox<Account> accountComboBox;
    AccountDialog accountDialog;
    OrderDialog orderDialog;
    Model model;

    public MyFrame() {
        connector.connect();
        connection = connector.getConnection();
        model = new Model();
        DBSync dbSync = new DBSync(connection, model);
        model.addListener(this);

        Container container = getContentPane();
        SpringLayout layout = new SpringLayout();
        container.setLayout(layout);

        orderDtm = new DefaultTableModel();
        Object[] columHeader = new String[]{"id", "Stock name", "Stock quantity", "Ask price",
            CANCEL_ORDER_COLUMN_HEADER};
        orderDtm.setColumnIdentifiers(columHeader);

        symbolDtm = new DefaultTableModel();
        Object[] symbolDtmColumnHeader = new String[]{"Symbol", "Ask", "Bid"};
        symbolDtm.setColumnIdentifiers(symbolDtmColumnHeader);

        accountComboBox = new JComboBox<>();
        accountComboBox.setPreferredSize(new Dimension(250, 20));
        container.add(accountComboBox);
        layout.putConstraint(SpringLayout.WEST, accountComboBox, 10, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, accountComboBox, 13, SpringLayout.NORTH, container);

        JButton addOrderButton = new JButton("Add new order");
        addOrderButton.addActionListener(e -> {
            orderDialog = new OrderDialog(this, "Create new order", true, new Order(-1,
                    ((Account) accountComboBox.getSelectedItem()).getId(), null, 0, 0,
                LocalDateTime.now()));
            orderDialog.setVisible(true);
            if(orderDialog.getOrder().getQuantity() != 0)
                model.addOrder(orderDialog.getOrder());
        });
        container.add(addOrderButton);
        layout.putConstraint(SpringLayout.SOUTH, addOrderButton, -20, SpringLayout.SOUTH, container);
        layout.putConstraint(SpringLayout.WEST, addOrderButton, 150, SpringLayout.WEST, container);

        JTable orderTable = new JTable(orderDtm);
        orderTable.getColumn(CANCEL_ORDER_COLUMN_HEADER).setCellRenderer(new ButtonRenderer());
        orderTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                row, column);
            c.setBackground(row % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
            return c;
            }
        });

        /*
          deletes a row in orderTable and record in DB by clicking "Cancel" in orderTable
         */
        orderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                if (target.isColumnSelected(CANCEL_ORDER_COLUMN_NUMBER)) {
                    int row = target.getSelectedRow();
                    int id = Integer.parseInt((String) orderDtm.getValueAt(row, ORDER_ID_COLUMN_NUMBER));
                    orderDtm.removeRow(row);
                    model.removeOrder((Account) accountComboBox.getSelectedItem(), id);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setPreferredSize(new Dimension(410, 200));
        container.add(scrollPane);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 45, SpringLayout.NORTH, container);

        JTable symbolTable = new JTable(symbolDtm);
        /*
            shows the "Create new order" dialog with pre-filled name and price of the stock by clicking on
            the cell in colum "ask"
         */
        symbolTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                int row = target.getSelectedRow();
                String name;
                name = (String) target.getValueAt(row, 0);
                int price = Integer.parseInt((String) target.getValueAt(row, 1));
                if (target.isColumnSelected(1)) {
                    orderDialog = new OrderDialog(MyFrame.this, "Create new order", true,
                        new Order(-1, ((Account) accountComboBox.getSelectedItem()).getId(), name,
                            0, price, LocalDateTime.now()));
                    orderDialog.setVisible(true);
                    model.addOrder(orderDialog.getOrder());
                }
            }
        });

        JScrollPane symbolScrollPane = new JScrollPane(symbolTable);
        symbolScrollPane.setPreferredSize(new Dimension(410, 200));
        container.add(symbolScrollPane);
        layout.putConstraint(SpringLayout.NORTH, symbolScrollPane, 5, SpringLayout.SOUTH, scrollPane);

        accountComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account acc = (Account) accountComboBox.getSelectedItem();
                if(acc != null)
                    updateOrderTable(acc.getOrders());
            }
        });

        JButton button = new JButton("Add new account");
        button.addActionListener(e -> {
        accountDialog = new AccountDialog(this, "Add new account", true);
            accountDialog.setVisible(true);
            model.addAccount(accountDialog.getAccount());
        });
        container.add(button);
        layout.putConstraint(SpringLayout.WEST, button, 10, SpringLayout.EAST, accountComboBox);
        layout.putConstraint(SpringLayout.NORTH, button, 10, SpringLayout.NORTH, container);

        setTitle("FirstSwingTask");
        setPreferredSize(new Dimension(430, 560));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        dbSync.loadFromDb();
        Timer timer = new Timer();
        UpdateScheduler scheduler = new UpdateScheduler();
        scheduler.addListener(this);
        timer.schedule(scheduler, 10, 1000);
        if(accountComboBox.getItemCount() != 0)
            accountComboBox.setSelectedIndex(0);
    }

    private void updateOrderTable(List<Order> orders) {
        try {
            orderDtm.setRowCount(0);
            for (Order order : orders) {
                String[] array = {String.valueOf(order.getId()), order.getName(),
                        String.valueOf(order.getQuantity()), String.valueOf(order.getAskPrice()), "Cancel"};
                orderDtm.addRow(array);
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("!!!");
        }
    }

    private void updateSymbolTable(List<Symbol> symbols) {
        try {
            symbolDtm.setRowCount(0);
            for (Symbol symbol : symbols) {
                String[] array = {symbol.getName(), String.valueOf(symbol.getAsk()),
                        String.valueOf(symbol.getBid())};
                symbolDtm.addRow(array);
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("!!!");
        }
    }

    @Override
    public void update() {
        model.updateSymbols();
    }

    @Override
    public void accountAdded(Account account) {
        accountComboBox.addItem(account);
    }

    @Override
    public void ordersChanged(List<Order> orders) {
        updateOrderTable(orders);
    }

    @Override
    public void orderRemoved(Order order) {
        updateOrderTable(model.findAccountById(order.getAccountId()).getOrders());
    }

    @Override
    public void symbolsAdded(List<Symbol> symbols) {
        updateSymbolTable(symbols);
    }

    public static void main(String[] args) {
        new MyFrame();
    }
}
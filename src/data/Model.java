package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Model {
    private int nextAccountId = 0;
    private int nextOrderId = 0;
    private List<ModelListener> listeners = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<Symbol> symbols = new ArrayList<>();

    public Model() {
        updateSymbols();
    }

    public void addAccount(Account account) {
        if(account != null) {
            if (account.getId() == -1) {
                account = new Account(generateAccountId(), account.getName(), account.getCreationTime());
            } else {
                if(nextAccountId < account.getId())
                    this.nextAccountId = account.getId();
            }
            accounts.add(account);
            accountAdded(account);
        }
    }

    public void accountAdded(Account account) {
        for (ModelListener listener : listeners) {
            listener.accountAdded(account);
        }
    }

    public void addOrder(Order order) {
        if (order.getId() == -1) {
            order = new Order(generateOrderId(), order.getAccountId(), order.getName(), order.getQuantity(),
                order.getAskPrice(), order.getCreationTime());
        }
        if(nextOrderId < order.getId())
            this.nextOrderId = order.getId();
        Account account = findAccountById(order.getAccountId());
        account.addOrder(order);
        ordersChanged(account.getOrders());
    }

    public void removeOrder(Account account, int orderId) {
        Order target = null;
        for (Order order : account.getOrders()) {
            if (order.getId() == orderId)
                target = order;
        }
        account.removeOrder(target);
        orderRemoved(target);
    }

    public void orderRemoved(Order order) {
        for (ModelListener listener : listeners) {
            listener.orderRemoved(order);
        }
    }

    public void ordersChanged(List<Order> orders) {
        for (ModelListener listener : listeners) {
            listener.ordersChanged(orders);
        }
    }

    public void updateSymbols() {
        Random random = new Random();
        symbols.clear();
        symbols.add(new Symbol("AAPL", random.nextInt(21) + 150, random.nextInt(21) + 150));
        symbols.add(new Symbol("FB", random.nextInt(10) + 210, random.nextInt(10) + 210));
        symbols.add(new Symbol("AMZN", random.nextInt(20) + 3200, random.nextInt(20) + 3200));
        symbolsAdded(symbols);
    }

    private void symbolsAdded(List<Symbol> symbols) {
        for(ModelListener listener: listeners) {
            listener.symbolsAdded(symbols);
        }
    }

    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    public Account findAccountById(int id) {
        Account target = null;
        if (!accounts.isEmpty()) {
            for (Account account : accounts) {
                if (account.getId() == id) {
                    target = account;
                    break;
                }
            }
        }
        return target;
    }

    private int generateAccountId() {
        nextAccountId++;
        return nextAccountId;
    }

    private int generateOrderId() {
        nextOrderId++;
        return nextOrderId;
    }
}
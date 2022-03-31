package db;

import data.*;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DBSync implements ModelListener {
    private Queries queries;
    private Model model;

    public DBSync(Connection connection, Model model) {
        queries = new Queries(connection);
        this.model = model;
        model.addListener(this);
    }

    public void loadFromDb() {
        List<Account> accountList = queries.getAccountsFromDb();
        for(Account account: accountList) {
            model.addAccount(account);
            for(Order order: queries.getOrders(account)) {
                try {
                    TimeUnit.MILLISECONDS.sleep(2);
                    model.addOrder(order);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void accountAdded(Account account) {
        queries.insert(account);
    }

    @Override
    public void ordersChanged(List<Order> orders) {
        for(Order order: orders)
            queries.insertOrder(order);
    }

    @Override
    public void orderRemoved(Order order) {
        queries.deleteOrder(order.getId());
    }

    @Override
    public void symbolsAdded(List<Symbol> symbols) {

    }
}

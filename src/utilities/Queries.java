package utilities;

import entities.Account;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Queries {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Connection connection;

    public Queries(Connection connection) {
        this.connection = connection;
    }

    public List<Account> getAccountsFromDb() {
        List<Account> accounts = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String getAccounts = "select * from accounts";
            ResultSet rs = statement.executeQuery(getAccounts);
            while (rs.next()) {
                accounts.add(new Account(rs.getInt(1), rs.getString(2),
                    LocalDateTime.parse(rs.getString(3), formatter)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public void getOrdersFromAccount(Account acc) {
        try (Statement statement = connection.createStatement()) {
            String getOrdersFromAccount = "select * from orders where account_id = " + acc.getId() + ";";
            ResultSet rs = statement.executeQuery(getOrdersFromAccount);
            while (rs.next()) {
                acc.addOrder(rs.getInt(1), rs.getInt(2),
                        rs.getString(3), rs.getInt(4),
                        rs.getInt(5), LocalDateTime.parse(rs.getString(6), formatter));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String accNameString, LocalDateTime creationTime) {
        try (Statement statement = connection.createStatement()) {
            String insert = "insert into accounts (name, creation_time) values ('" + accNameString + "', '" +
                creationTime.format(formatter) + "');";
            statement.execute(insert);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Account getId(String accNameString, LocalDateTime creationTime) {
        Account account = null;
        int id = 0;
        try (Statement statement = connection.createStatement()) {
            String getId = "select id from accounts where name in ('" + accNameString + "') and creation_time = '" +
               creationTime.format(formatter) + "';";
            ResultSet rs = statement.executeQuery(getId);
            while (rs.next())
                id = rs.getInt(1);
            account = new Account(id, accNameString, creationTime);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return account;
    }

    public void insertOrder(Account account, String stockName, int stockQuantity, int stockPrice,
                            LocalDateTime creationTime) {
        try (Statement statement = connection.createStatement()) {
            String insertOrder = "insert into orders (account_id, stock_name, quantity, ask_price, creation_time) " +
                    "values (" + account.getId() + ", '" + stockName + "' ," + stockQuantity + ", " +
                    stockPrice + ", '" + creationTime.format(formatter) + "');";
            statement.execute(insertOrder);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOrder(Account account, String stockName, int stockQuantity, int stockPrice,
                         LocalDateTime creationTime) {
        int orderId = 0;
        try (Statement statement = connection.createStatement()) {
            String getOrderId = "select id from orders where account_id = " + account.getId() +
                    " and stock_name = '" + stockName + "' and quantity = " + stockQuantity +
                    " and ask_price = " + stockPrice + " and creation_time = '" + creationTime.format(formatter) + "';";
            ResultSet id = statement.executeQuery(getOrderId);
            while (id.next()) {
                orderId = id.getInt(1);
            }
            account.addOrder(orderId, account.getId(), stockName, stockQuantity, stockPrice, creationTime);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String[]> getOrders(Account account) {
        List<String[]> ordersArrays = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String selectOrders = "select id, stock_name, quantity, ask_price from orders where account_id = " +
                    account.getId() + ";";
            ResultSet rs = statement.executeQuery(selectOrders);
            while (rs.next()) {
                ordersArrays.add(new String[]{String.valueOf(rs.getInt(1)), rs.getString(2),
                        String.valueOf(rs.getInt(3)), String.valueOf(rs.getInt(4)), "Cancel"});
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("getOrders()");
        }
        return ordersArrays;
    }

    public void deleteOrder(int id) {
        try (Statement statement = connection.createStatement()) {
            String deleteOrder = "delete from orders where id = " + id;
            statement.execute(deleteOrder);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Queries.deleteOrder()");
        }
    }
}
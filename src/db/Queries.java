package db;

import data.Account;
import data.Order;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for SQL queries
 */
public class Queries{
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

    public void insert(Account account) {
        try {
            String insert = "insert ignore into accounts (id, name, creation_time) values (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setInt(1, account.getId());
            preparedStatement.setString(2, account.getName());
            preparedStatement.setString(3, account.getCreationTime().format(formatter));
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertOrder(Order order) {
        try {
            String insertOrder = "insert ignore into orders (id, account_id, stock_name, quantity, ask_price, creation_time) " +
                    "values (?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertOrder);
            preparedStatement.setInt(1, order.getId());
            preparedStatement.setInt(2, order.getAccountId());
            preparedStatement.setString(3, order.getName());
            preparedStatement.setInt(4, order.getQuantity());
            preparedStatement.setInt(5, order.getAskPrice());
            preparedStatement.setString(6, order.getCreationTime().format(formatter));
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Order> getOrders(Account account) {
        List<Order> orders = new ArrayList<>();
        try {
            String selectOrders = "select id, account_id, stock_name, quantity, ask_price, creation_time from orders where account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(selectOrders);
            preparedStatement.setInt(1, account.getId());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                orders.add(new Order(rs.getInt(1), rs.getInt(2), rs.getString(3),
                        rs.getInt(4), rs.getInt(5), LocalDateTime.parse(rs.getString(6), formatter)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("getOrders()");
        }
        return orders;
    }

    public void deleteOrder(int id) {
        try {
            String deleteOrder = "delete from orders where id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteOrder);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Queries.deleteOrder()");
        }
    }
}
package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private int id = 0;
    private String name;
    private LocalDateTime creationTime;
    private List<Order> orders = new ArrayList<>();

    public Account(int id, String name, LocalDateTime creationTime) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
    }

    public  LocalDateTime getCreationTime() {
        return creationTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public void addOrder(int orderId, int accountId, String stockName, int quantity, int askPrice,
                         LocalDateTime orderCreationTime) {
        //int bidId = generator.generate();
        orders.add(new Order(orderId, accountId, stockName, quantity, askPrice, orderCreationTime));
    }
}
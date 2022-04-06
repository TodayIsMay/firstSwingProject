package data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * entity for user's accounts
 */
public class Account extends Entity{
    private static final int DEFAULT_ID = -1;
    private List<Order> orders = new ArrayList<>();

    public Account(int id, String name, LocalDateTime creationTime) {
        super(id, name, creationTime);
    }

    public Account(String name, LocalDateTime creationTime) {
        this(DEFAULT_ID, name, creationTime);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }
}
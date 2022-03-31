package data;

import java.time.LocalDateTime;

/**
 * entity for orders, which were created in account
 */
public class Order extends Entity{
    private static final int DEFAULT_ID = -1;
    private int accountId;
    private int quantity;
    private int askPrice;

    public Order(int id, int accountId, String name, int quantity, int askPrice, LocalDateTime creationTime) {
        super(id, name, creationTime);
        this.accountId = accountId;
        this.quantity = quantity;
        this.askPrice = askPrice;
    }

    public Order(int accountId, String stockName, int quantity, int askPrice, LocalDateTime creationTime) {
        this(DEFAULT_ID, accountId, stockName, quantity, askPrice,creationTime);
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(int askPrice) {
        this.askPrice = askPrice;
    }
}
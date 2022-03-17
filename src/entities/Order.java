package entities;

public class Order {
    private int id;
    private int accountId;
    private String stockName;
    private int quantity;
    private int askPrice;

    public Order(int id, int accountId, String stockName, int quantity, int askPrice) {
        this.id = id;
        this.accountId = accountId;
        this.stockName = stockName;
        this.quantity = quantity;
        this.askPrice = askPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
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

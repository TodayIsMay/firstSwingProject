import java.util.ArrayList;
import java.util.List;

public class Account {
    Generator generator = new Generator();
    private int id = 0;
    private String name;
    private List<Bid> bids = new ArrayList<>();

    public Account(int id, String name) {
        this.id = id;
        this.name = name;
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

    public List<Bid> getBids() {
        return bids;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public void addBid(int accountId, String stockName, int quantity, int askPrice) {
        int bidId = generator.generate();
        bids.add(new Bid(bidId, accountId, stockName, quantity, askPrice));
    }
}

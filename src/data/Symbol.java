package data;

public class Symbol {
    private String name;
    private int ask;
    private int bid;

    public Symbol(String name, int ask, int bid) {
        this.name = name;
        this.ask = ask;
        this.bid = bid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAsk() {
        return ask;
    }

    public void setAsk(int ask) {
        this.ask = ask;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }
}

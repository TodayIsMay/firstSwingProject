package utilities;

public class Generator {
    private int id = 0;

    public int generate() {
        this.id++;
        return id;
    }
}

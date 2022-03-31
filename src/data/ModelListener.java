package data;

import java.util.List;

public interface ModelListener {
    void accountAdded(Account account);

    void ordersChanged(List<Order> orders);

    void orderRemoved(Order order);

    void symbolsAdded(List<Symbol> symbols);
}

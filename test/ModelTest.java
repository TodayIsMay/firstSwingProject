import data.Account;
import data.Model;
import data.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ModelTest {
    Model model;
    Account account;
    Order order;
    private static final LocalDateTime CREATION_TIME = LocalDateTime.of(
            2020, 2, 20, 20, 20);

    @BeforeEach
    public void init() {
        model = new Model();
        account = new Account(1, "account", CREATION_TIME);
        order = new Order(1, 1, "AAPL", 20, 200, CREATION_TIME);
    }

    @Test
    public void shouldAddAccount() {
        model.addAccount(account);
        List<Account> expected = new ArrayList<>();
        expected.add(account);
        List<Account> actual = model.getAccounts();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldNotifyListenersIdAnAccountAdded() {
        Assertions.assertTrue(model.addAccount(account));
    }

    @Test
    public void shouldNotAddAccountWhenNull() {
        account = null;
        model.addAccount(account);
        Assertions.assertEquals(0, model.getAccounts().size());
    }

    @Test
    public void shouldGenerateId1() {
        model.addAccount(new Account(-1, "account", CREATION_TIME));
        Account expected = account;
        Account actual = model.getAccounts().get(0);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnNullIfThereIsNoAccount() {
        Assertions.assertNull(model.findAccountById(0));
    }

    @Test
    public void shouldAddOrder() {
        model.addAccount(account);
        boolean result = model.addOrder(order);
        List<Order> expected = new ArrayList<>();
        expected.add(order);
        List<Order> actual = account.getOrders();
        Assertions.assertEquals(expected, actual);
        Assertions.assertTrue(result);
    }

    @Test
    public void shouldNotAddOrderForNotExistingAccount() {
        Assertions.assertFalse(model.addOrder(order));
    }

    @Test
    public void shouldGenerateOrderId1() {
        model.addAccount(account);
        model.addOrder(new Order(-1, 1, "AAPL", 10, 10, CREATION_TIME));
        Order expected = order;
        Order actual = model.findAccountById(1).getOrders().get(0);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldRemoveOrder() {
        model.addAccount(account);
        model.addOrder(order);
        model.removeOrder(account, 1);
        Assertions.assertEquals(0, account.getOrders().size());
    }

    @Test
    public void shouldReturnNullIfThereIsNoOrder() {
        model.addAccount(account);
        Assertions.assertNull(model.findOrderById(0));
    }

    @Test
    public void shouldReturnNullIfThereAreNoAccounts() {
        Assertions.assertNull(model.findAccountById(0));
    }
}

package services;

import entities.Bottom;
import entities.Orderline;
import entities.Topping;
import entities.User;
import exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderServiceTest {
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        // calculateTotal bruger ikke databasen, så vi behøver ingen rigtig ConnectionPool
        orderService = new OrderService(null);
    }

    @Test
    void emptyCartGivesZero() {
        assertEquals(0, orderService.calculateTotal(new ArrayList<>()), 0.001);
    }

    @Test
    void missingCartGivesZero() {
        assertEquals(0, orderService.calculateTotal(null), 0.001);
    }

    @Test
    void oneLineIsUnitPriceTimesQuantity() {
        List<Orderline> cart = new ArrayList<>();
        cart.add(new Orderline(new Bottom(1, "Chokolade", 5), new Topping(2, "Blåbær", 5), 2));

        // (5 + 5) * 2 = 20
        assertEquals(20, orderService.calculateTotal(cart), 0.001);
    }

    @Test
    void totalIsSumOfAllLines() {
        List<Orderline> cart = new ArrayList<>();
        cart.add(new Orderline(new Bottom(1, "Chokolade", 5), new Topping(2, "Blåbær", 5), 2));
        cart.add(new Orderline(new Bottom(5, "Mandel", 7), new Topping(8, "Citron", 8), 3));

        // 20 + (7 + 8) * 3 = 65
        assertEquals(65, orderService.calculateTotal(cart), 0.001);
    }

    @Test
    void notEnoughMoneyThrowsException(){
        User user = new User(1, "test@test.dk", "1234", 10, "customer");
        List<Orderline> cart = new ArrayList<>();
        cart.add(new Orderline(new Bottom(1, "Chokolade", 5), new Topping(2, "Blåbær", 5), 2));
        assertThrows(DatabaseException.class, () -> orderService.placeOrder(user, cart));
    }
}

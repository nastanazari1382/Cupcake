package persistence;

import entities.Bottom;
import entities.Order;
import entities.OrderSummary;
import entities.Orderline;
import entities.Topping;
import exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {
    private static final ConnectionPool connectionPool = TestDatabase.connectionPool;

    // Creates the test tables once before all tests in this class
    @BeforeAll
    static void setUpClass() throws SQLException {
        TestDatabase.createTables();
    }

    // Resets the test data before each test
    @BeforeEach
    void setUp() throws SQLException {
        TestDatabase.insertTestData();
    }

    @Test
    void createOrderSavesPaidOrderWithLine() throws DatabaseException {
        Bottom bottom = new Bottom(1, "Vanilje", 15);
        Topping topping = new Topping(1, "Jordbær", 14);

        int orderId = OrderMapper.createOrder(3, connectionPool);
        OrderMapper.createOrderline(orderId, new Orderline(bottom, topping, 3), connectionPool);

        List<Order> orders = OrderMapper.getOrdersByUserId(3, connectionPool);
        assertEquals(1, orders.size());
        assertTrue(orders.get(0).isPaid());
        assertEquals(1, orders.get(0).getLines().size());
        assertEquals(87, orders.get(0).getTotal(), 0.001); // 3 x (15 + 14)
    }

    @Test
    void getAllOrdersReturnsEmailAndTotal() throws DatabaseException {
        List<OrderSummary> orders = OrderMapper.getAllOrders(connectionPool);
        assertEquals(1, orders.size());
        assertEquals("kunde@mail.dk", orders.get(0).getEmail());
        assertEquals(58, orders.get(0).getTotalPrice(), 0.001); // 2 x 29
    }

    @Test
    void getOrdersByUserIdUsesSavedUnitPrice() throws DatabaseException {
        List<Order> orders = OrderMapper.getOrdersByUserId(2, connectionPool);
        Orderline line = orders.get(0).getLines().get(0);
        assertEquals("Chokolade", line.getBottom().getName());
        assertEquals(29, line.getUnitPrice(), 0.001);
    }

    @Test
    void getOrdersByUserIdWithoutOrdersIsEmpty() throws DatabaseException {
        assertTrue(OrderMapper.getOrdersByUserId(1, connectionPool).isEmpty());
    }

    // Without ON DELETE CASCADE the delete would fail, because the order still has an orderline
    @Test
    void deleteOrderAlsoDeletesOrderlines() throws DatabaseException {
        OrderMapper.deleteOrder(1, connectionPool);
        assertTrue(OrderMapper.getAllOrders(connectionPool).isEmpty());
        assertTrue(OrderMapper.getOrdersByUserId(2, connectionPool).isEmpty());
    }

    @Test
    void deleteOrderThatDoesNotExistThrows() {
        assertThrows(DatabaseException.class, () -> OrderMapper.deleteOrder(99, connectionPool));
    }
}

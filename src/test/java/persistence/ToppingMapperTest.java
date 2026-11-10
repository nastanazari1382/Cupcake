package persistence;

import entities.Topping;
import exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ToppingMapperTest {
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
    void getAllToppingsReturnsAll() throws DatabaseException {
        assertEquals(2, ToppingMapper.getAllToppings(connectionPool).size());
    }

    @Test
    void getToppingByIdReturnsNameAndPrice() throws DatabaseException {
        Topping topping = ToppingMapper.getToppingById(2, connectionPool);
        assertEquals("Mint", topping.getName());
        assertEquals(14, topping.getPrice(), 0.001);
    }
}

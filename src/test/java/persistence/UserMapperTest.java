package persistence;

import entities.User;
import exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
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
    void loginWithCorrectPasswordReturnsUser() throws DatabaseException {
        User user = UserMapper.login("kunde@mail.dk", "1234", connectionPool);
        assertEquals(2, user.getId());
        assertEquals("customer", user.getRole());
        assertEquals(200, user.getBalance(), 0.001);
    }

    @Test
    void loginWithWrongPasswordThrows() {
        assertThrows(DatabaseException.class, () -> UserMapper.login("kunde@mail.dk", "forkert", connectionPool));
    }

    @Test
    void createUserGivesCustomerWithZeroBalance() throws DatabaseException {
        User user = UserMapper.createUser("ny@mail.dk", "abc", connectionPool);
        assertEquals(4, user.getId());
        assertEquals("customer", user.getRole());
        assertEquals(0, user.getBalance(), 0.001);
    }

    @Test
    void createUserWithExistingEmailThrows() {
        assertThrows(DatabaseException.class, () -> UserMapper.createUser("kunde@mail.dk", "abc", connectionPool));
    }

    @Test
    void updateBalanceSavesNewBalance() throws DatabaseException {
        UserMapper.updateBalance(2, 150, connectionPool);
        User user = UserMapper.login("kunde@mail.dk", "1234", connectionPool);
        assertEquals(150, user.getBalance(), 0.001);
    }

    @Test
    void getAllCustomersDoesNotIncludeAdmin() throws DatabaseException {
        List<User> customers = UserMapper.getAllCustomers(connectionPool);
        assertEquals(2, customers.size());
        for (User customer : customers) {
            assertEquals("customer", customer.getRole());
        }
    }
}

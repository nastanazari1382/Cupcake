package persistence;

import entities.Bottom;
import exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BottomMapperTest {
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
    void getAllBottomsReturnsAll() throws DatabaseException {
        assertEquals(2, BottomMapper.getAllBottoms(connectionPool).size());
    }

    @Test
    void getBottomByIdReturnsNameAndPrice() throws DatabaseException {
        Bottom bottom = BottomMapper.getBottomById(2, connectionPool);
        assertEquals("Chokolade", bottom.getName());
        assertEquals(15, bottom.getPrice(), 0.001);
    }
}

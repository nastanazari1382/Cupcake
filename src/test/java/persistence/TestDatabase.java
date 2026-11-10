package persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

// Sets up the test schema, so the mapper tests never touch the real data in "public"
public class TestDatabase {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=test";
    private static final String DB = "cupcake";

    // The same pool is used by all test classes (ConnectionPool is a singleton)
    public static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    // Creates the schema "test" and the same tables as in cupcake.sql. Runs once per test class
    public static void createTables() throws SQLException {
        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("CREATE SCHEMA IF NOT EXISTS test");
            stmt.execute("DROP TABLE IF EXISTS test.orderlines, test.orders, test.toppings, test.bottoms, test.users");

            stmt.execute("""
                    CREATE TABLE test.users (
                        id       SERIAL PRIMARY KEY,
                        email    VARCHAR(255)   NOT NULL UNIQUE,
                        password VARCHAR(255)   NOT NULL,
                        balance  NUMERIC(10, 2) NOT NULL DEFAULT 0 CHECK (balance >= 0),
                        role     VARCHAR(10)    NOT NULL DEFAULT 'customer' CHECK (role IN ('customer', 'admin'))
                    )""");
            stmt.execute("""
                    CREATE TABLE test.bottoms (
                        id    SERIAL PRIMARY KEY,
                        name  VARCHAR(50)   NOT NULL UNIQUE,
                        price NUMERIC(5, 2) NOT NULL
                    )""");
            stmt.execute("""
                    CREATE TABLE test.toppings (
                        id    SERIAL PRIMARY KEY,
                        name  VARCHAR(50)   NOT NULL UNIQUE,
                        price NUMERIC(5, 2) NOT NULL
                    )""");
            stmt.execute("""
                    CREATE TABLE test.orders (
                        id         SERIAL PRIMARY KEY,
                        user_id    INT       NOT NULL REFERENCES test.users (id),
                        order_date TIMESTAMP NOT NULL DEFAULT NOW(),
                        paid       BOOLEAN   NOT NULL DEFAULT FALSE,
                        picked_up  BOOLEAN   NOT NULL DEFAULT FALSE
                    )""");
            stmt.execute("""
                    CREATE TABLE test.orderlines (
                        id         SERIAL PRIMARY KEY,
                        order_id   INT           NOT NULL REFERENCES test.orders (id) ON DELETE CASCADE,
                        bottom_id  INT           NOT NULL REFERENCES test.bottoms (id),
                        topping_id INT           NOT NULL REFERENCES test.toppings (id),
                        quantity   INT           NOT NULL CHECK (quantity > 0),
                        unit_price NUMERIC(6, 2) NOT NULL
                    )""");
        }
    }

    // Empties the tables and inserts the same test data before every test, so the tests do not affect each other
    public static void insertTestData() throws SQLException {
        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement()) {

            // RESTART IDENTITY starts the ids from 1 again
            stmt.execute("TRUNCATE test.orderlines, test.orders, test.toppings, test.bottoms, test.users RESTART IDENTITY");

            stmt.execute("""
                    INSERT INTO test.users (email, password, balance, role) VALUES
                    ('admin@cupcake.dk', '1234', 0, 'admin'),
                    ('kunde@mail.dk', '1234', 200, 'customer'),
                    ('kunde2@mail.dk', 'abcd', 50, 'customer')""");
            stmt.execute("INSERT INTO test.bottoms (name, price) VALUES ('Vanilje', 15), ('Chokolade', 15)");
            stmt.execute("INSERT INTO test.toppings (name, price) VALUES ('Jordbær', 14), ('Mint', 14)");

            // One paid order for kunde@mail.dk (id 2): 2 x Chokolade + Mint at 29 kr
            stmt.execute("INSERT INTO test.orders (user_id, paid) VALUES (2, true)");
            stmt.execute("INSERT INTO test.orderlines (order_id, bottom_id, topping_id, quantity, unit_price) VALUES (1, 2, 2, 2, 29)");
        }
    }
}

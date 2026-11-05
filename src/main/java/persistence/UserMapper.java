package persistence;

import entities.User;
import exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    // Finds the user with the given email and password, or throws if login fails
    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                double balance = rs.getDouble("balance");
                String role = rs.getString("role");
                return new User(id,email,password,balance,role);
            } else {
                throw new DatabaseException("Forkert email eller kode");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved login: " + e.getMessage());
        }
    }


    // Creates a new customer. role = 'customer' and balance = 0 come from DEFAULT in the table
    public static User createUser(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO users (email, password) VALUES (?, ?) RETURNING id, balance, role";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return new User(rs.getInt("id"), email, password, rs.getDouble("balance"), rs.getString("role"));
        } catch (SQLException e) {
            if (UNIQUE_VIOLATION.equals(e.getSQLState())) {
                throw new DatabaseException("Der findes allerede en konto med den email");
            }
            throw new DatabaseException("Fejl ved oprettelse af konto: " + e.getMessage());
        }
    }

    // Sets the customer's new balance when they pay
    public static void updateBalance(int userId, double newBalance, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, newBalance);
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();
            if (rows != 1) {
                throw new DatabaseException("Saldoen kunne ikke opdateres");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved opdatering af saldo: " + e.getMessage());
        }
    }

    // This method gets all users with the role 'customer'
    public static List<User> getAllCustomers(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT id, email, password, balance, role FROM users WHERE role = 'customer' ORDER BY email";
        List<User> customers = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                customers.add(new User(rs.getInt("id"), rs.getString("email"), rs.getString("password"),
                        rs.getDouble("balance"), rs.getString("role")));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af kunder: " + e.getMessage());
        }
        return customers;
    }


    // Postgres error code for "unique constraint violated" (here: the email already exists)
    private static final String UNIQUE_VIOLATION = "23505";

}
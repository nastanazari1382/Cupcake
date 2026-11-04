package persistence;

import entities.User;
import exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

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


    // NY til US-2: role = 'customer' og balance = 0 kommer fra DEFAULT i tabellen
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

    // NY til US-1: sætter kundens nye saldo, når der betales
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

    // Postgres' fejlkode for "unique constraint overtrådt" (her: emailen findes allerede)
    private static final String UNIQUE_VIOLATION = "23505";

}
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

}
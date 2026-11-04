package persistence;

import exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper {
    public static int createOrder(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orders (user_id) VALUES (?) RETURNING id";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new DatabaseException("Ordren kunne ikke oprettes");

        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved oprettelse af ordre: " + e.getMessage());
        }
    }
}

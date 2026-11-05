package persistence;

import entities.Bottom;
import exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BottomMapper {

    // Gets all bottoms from the database
    public static List<Bottom> getAllBottoms(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM bottoms";
        List<Bottom> bottoms = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                bottoms.add(new Bottom(id, name, price));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indhentning af bunde: " + e.getMessage());
        }
        return bottoms;
    }


    public static Bottom getBottomById(int id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM bottoms WHERE id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                return new Bottom(id, name, price);
            } else {
                throw new DatabaseException("Bunden findes ikke");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indhentning af bund: " + e.getMessage());
        }
    }
}

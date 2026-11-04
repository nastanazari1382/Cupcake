package persistence;

import entities.Topping;
import exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ToppingMapper {

    // Gets all toppings from the database
    public static List<Topping> getAllToppings(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM toppings";
        List<Topping> toppings = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                toppings.add(new Topping(id, name, price));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indhentning af toppe: " + e.getMessage());
        }
        return toppings;

    }
}


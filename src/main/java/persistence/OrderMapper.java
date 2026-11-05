package persistence;

import entities.OrderSummary;
import entities.Orderline;
import exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import entities.Bottom;
import entities.Order;
import entities.Orderline;
import entities.Topping;


public class OrderMapper {
    // Creates a new order for the user and returns its id
    public static int createOrder(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orders (user_id) VALUES (?) RETURNING id";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

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

    // Saves one orderline (bottom, topping, quantity and unit price) on the order
    public static void createOrderline(int orderId, Orderline line, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orderlines (order_id, bottom_id, topping_id, quantity, unit_price) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, line.getBottom().getId());
            ps.setInt(3, line.getTopping().getId());
            ps.setInt(4, line.getQuantity());
            ps.setDouble(5, line.getUnitPrice());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved oprettelse af ordrelinje: " + e.getMessage());
        }
    }

    // Gets all orders with the customer's email and total price, newest first
    public static List<OrderSummary> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        String sql = """
                SELECT orders.id, users.email, orders.order_date, orders.paid, orders.picked_up,
                       SUM(orderlines.quantity * orderlines.unit_price) AS total_price
                FROM orders
                JOIN users ON orders.user_id = users.id
                JOIN orderlines ON orderlines.order_id = orders.id
                GROUP BY orders.id, users.email
                ORDER BY orders.order_date DESC
                """;
        List<OrderSummary> orderSummaryList = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email");
                LocalDateTime orderDate = rs.getTimestamp("order_date").toLocalDateTime();
                double totalPrice = rs.getDouble("total_price");
                boolean paid = rs.getBoolean("paid");
                boolean pickedUp = rs.getBoolean("picked_up");
                orderSummaryList.add(new OrderSummary(id, email, orderDate, totalPrice, paid, pickedUp));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indhentning af ordrer: " + e.getMessage());
        }
        return orderSummaryList;
    }

    // Deletes an order.
    public static void deleteOrder(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM orders WHERE id = ? ";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Ordren kunne ikke slettes");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved sletning af ordre: " + e.getMessage());
        }
    }


    //gets all orders (with their orderlines) for one customer, newest first
    public static List<Order> getOrdersByUserId(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT o.id, o.user_id, o.order_date, o.paid, o.picked_up, "
                + "       b.id AS bottom_id, b.name AS bottom_name, "
                + "       t.id AS topping_id, t.name AS topping_name, "
                + "       ol.quantity, ol.unit_price "
                + "FROM orders o "
                + "LEFT JOIN orderlines ol ON ol.order_id = o.id "
                + "LEFT JOIN bottoms b ON b.id = ol.bottom_id "
                + "LEFT JOIN toppings t ON t.id = ol.topping_id "
                + "WHERE o.user_id = ? "
                + "ORDER BY o.id DESC, ol.id";

        List<Order> orders = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            Order current = null;

            while (rs.next()) {
                int orderId = rs.getInt("id");

                // a new order starts when the id changes
                if (current == null || current.getId() != orderId) {
                    current = new Order(orderId, rs.getInt("user_id"),
                            rs.getTimestamp("order_date").toLocalDateTime(),
                            rs.getBoolean("paid"), rs.getBoolean("picked_up"));
                    orders.add(current);
                }

                // add this row as an orderline on the current order (NULL = order without lines)
                if (rs.getObject("bottom_id") != null) {
                    Bottom bottom = new Bottom(rs.getInt("bottom_id"), rs.getString("bottom_name"), 0);
                    Topping topping = new Topping(rs.getInt("topping_id"), rs.getString("topping_name"), 0);

                    current.addLine(new Orderline(bottom, topping, rs.getInt("quantity"), rs.getDouble("unit_price")));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indhentning af ordrer: " + e.getMessage());
        }
        return orders;
    }
}

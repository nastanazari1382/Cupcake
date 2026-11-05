package services;

import entities.Bottom;
import entities.Orderline;
import entities.Topping;
import entities.User;
import exceptions.DatabaseException;
import persistence.BottomMapper;
import persistence.ConnectionPool;
import persistence.OrderMapper;
import persistence.ToppingMapper;
import persistence.UserMapper;

import java.util.List;

public class OrderService {
    private final ConnectionPool connectionPool;

    // The pool is passed on to the mappers
    public OrderService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    // Returns the sum of all orderlines in the cart. An empty or missing cart gives 0.
    public double calculateTotal(List<Orderline> cart) {
        if (cart == null) {
            return 0;
        }

        double total = 0;
        for (Orderline line : cart) {
            total += line.getTotalPrice();
        }
        return total;
    }

    // Places the order: saves the order and its orderlines and withdraws the amount from the user's balance
    public void placeOrder(User user, List<Orderline> cart) throws DatabaseException {
        if (cart == null || cart.isEmpty()) {
            throw new DatabaseException("Kurven er tom");
        }

        double total = calculateTotal(cart);
        if (user.getBalance() < total) {
            throw new DatabaseException("Du har ikke nok penge på kontoen");
        }

        int orderId = OrderMapper.createOrder(user.getId(), connectionPool);
        for (Orderline line : cart) {
            OrderMapper.createOrderline(orderId, line, connectionPool);
        }

        double newBalance = user.getBalance() - total;
        UserMapper.updateBalance(user.getId(), newBalance, connectionPool);
        user.setBalance(newBalance);
    }

    // Adds a new orderline to the cart. The same bottom and topping twice gives two separate lines.
    public void addToCart(List<Orderline> cart, int bottomId, int toppingId, int quantity) throws DatabaseException {
        if (quantity < 1) {
            throw new DatabaseException("Antal skal være mindst 1");
        }

        Bottom bottom = BottomMapper.getBottomById(bottomId, connectionPool);
        Topping topping = ToppingMapper.getToppingById(toppingId, connectionPool);

        cart.add(new Orderline(bottom, topping, quantity));
    }
}

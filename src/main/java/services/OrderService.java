package services;

import entities.Orderline;
import persistence.ConnectionPool;

import java.util.List;

public class OrderService {
    private final ConnectionPool connectionPool;

    // The pool is used later when orders are saved
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
}

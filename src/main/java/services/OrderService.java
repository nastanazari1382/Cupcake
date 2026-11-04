package services;

import entities.Orderline;
import persistence.ConnectionPool;

import java.util.List;

public class OrderService {
    private final ConnectionPool connectionPool;

    public OrderService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    // Summen af alle ordrelinjer i kurven. En tom eller manglende kurv giver 0.
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

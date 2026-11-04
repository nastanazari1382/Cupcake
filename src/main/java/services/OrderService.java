package services;

import entities.Orderline;
import entities.User;
import exceptions.DatabaseException;
import persistence.ConnectionPool;
import persistence.OrderMapper;
import persistence.UserMapper;

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

    public void placeOrder(User user, List<Orderline>cart)throws DatabaseException {
    double total = calculateTotal(cart);
    if(user.getBalance()<total){
        throw new DatabaseException("Du har ikke nok penge på kontoen");
    }
        int orderId = OrderMapper.createOrder(user.getId(),connectionPool);
        for(Orderline line : cart){
            OrderMapper.createOrderline(orderId, line, connectionPool);
        }
        double newBalance = user.getBalance() - total;
        UserMapper.updateBalance(user.getId(), newBalance, connectionPool);
        user.setBalance(newBalance);
    }

}

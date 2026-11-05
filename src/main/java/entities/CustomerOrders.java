package entities;

import java.util.List;

// One customer together with that customer's orders (used by the admin customer page)
public class CustomerOrders {
    private final User customer;
    private final List<Order> orders;

    public CustomerOrders(User customer, List<Order> orders) {
        this.customer = customer;
        this.orders = orders;
    }

    public User getCustomer() {
        return customer;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
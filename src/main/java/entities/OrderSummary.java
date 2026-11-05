package entities;

import java.time.LocalDateTime;

// An order as the admin sees it in the order list, with the customer's email and the total price.
public class OrderSummary {
    private int id;
    private String email;
    private LocalDateTime orderDate;
    private double totalPrice;
    private boolean paid;
    private boolean pickedUp;

    public OrderSummary(int id, String email, LocalDateTime orderDate, double totalPrice, boolean paid, boolean pickedUp) {
        this.id = id;
        this.email = email;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.paid = paid;
        this.pickedUp = pickedUp;
    }

    public int getId() {
        return id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public boolean isPaid() {
        return paid;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }
}

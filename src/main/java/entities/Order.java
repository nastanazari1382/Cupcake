package entities;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private int userId;
    private LocalDateTime orderDate;
    private boolean paid;
    private boolean pickedUp;

    public Order(int id, int userId, LocalDateTime orderDate, boolean paid, boolean pickedUp) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.paid = paid;
        this.pickedUp = pickedUp;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
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

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }
}

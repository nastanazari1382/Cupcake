package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private int userId;
    private LocalDateTime orderDate;
    private boolean paid;
    private boolean pickedUp;
    private final List<Orderline> lines = new ArrayList<>();

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

    public void addLine(Orderline line) {
        lines.add(line);
    }

    public List<Orderline> getLines() {
        return lines;
    }

    // Total price of all lines on the order
    public double getTotal() {
        double sum = 0;
        for (Orderline line : lines) {
            sum += line.getTotalPrice();
        }
        return sum;
    }

}


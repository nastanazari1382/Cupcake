package entities;

public class Orderline {
    private Bottom bottom;
    private Topping topping;
    private int quantity;
    private double unitPrice;

    // New orderline in the cart: the unit price is the current price of bottom + topping
    public Orderline(Bottom bottom, Topping topping, int quantity) {
        this(bottom, topping, quantity, bottom.getPrice() + topping.getPrice());
    }

    // Orderline from the database: uses the unit price saved when the order was placed
    public Orderline(Bottom bottom, Topping topping, int quantity, double unitPrice) {
        this.bottom = bottom;
        this.topping = topping;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Bottom getBottom() {
        return bottom;
    }

    public Topping getTopping() {
        return topping;
    }

    public int getQuantity() {
        return quantity;
    }

    // Price for one cupcake
    public double getUnitPrice() {
        return unitPrice;
    }

    // Price for the whole line: unit price * quantity
    public double getTotalPrice() {
        return getUnitPrice() * quantity;
    }
}

package entities;

public class Orderline {
    private Bottom bottom;
    private Topping topping;
    private int quantity;
    private double unitPrice;

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

    // Price for one cupcake: bottom + topping
    public double getUnitPrice() {
        return bottom.getPrice() + topping.getPrice();
    }


    // Price for the whole line: unit price * quantity
    public double getTotalPrice() {
        return getUnitPrice() * quantity;
    }
}

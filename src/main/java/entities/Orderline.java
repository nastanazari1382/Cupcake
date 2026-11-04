package entities;

public class Orderline {
    private Bottom bottom;
    private Topping topping;
    private int quantity;

    public Orderline(Bottom bottom, Topping topping, int quantity) {
        this.bottom = bottom;
        this.topping = topping;
        this.quantity = quantity;
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

    public double getUnitPrice() {
        return bottom.getPrice() + topping.getPrice();
    }


    public double getTotalPrice() {
        return getUnitPrice() * quantity;
    }
}

package controllers;

import entities.Orderline;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import persistence.ConnectionPool;
import services.OrderService;

import java.util.List;

public class OrderController {
    private final OrderService orderService;

    public OrderController(ConnectionPool connectionPool) {
        this.orderService = new OrderService(connectionPool);
    }

    // Registers the routes for the cart
    public void setRoutes(JavalinConfig config) {
        config.routes.get("/kurv", ctx -> showCart(ctx));
    }

    // Shows the cart with all orderlines and the total price
    private void showCart(Context ctx) {
        List<Orderline> cart = ctx.sessionAttribute("cart");
        double total = orderService.calculateTotal(cart);

        ctx.attribute("cart", cart);
        ctx.attribute("total", total);
        ctx.render("cart.html");
    }
}

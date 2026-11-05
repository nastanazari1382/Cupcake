package controllers;

import entities.Orderline;
import exceptions.DatabaseException;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import persistence.ConnectionPool;
import services.OrderService;

import java.util.ArrayList;
import java.util.List;

public class OrderController {
    private final OrderService orderService;

    public OrderController(ConnectionPool connectionPool) {
        this.orderService = new OrderService(connectionPool);
    }

    // Registers the routes for the cart
    public void setRoutes(JavalinConfig config) {
        config.routes.get("/kurv", ctx -> showCart(ctx));
        config.routes.post("/kurv/tilfoej", ctx -> addToCart(ctx));
    }

    // Shows the cart with all orderlines and the total price
    private void showCart(Context ctx) {
        List<Orderline> cart = ctx.sessionAttribute("cart");
        double total = orderService.calculateTotal(cart);

        ctx.attribute("cart", cart);
        ctx.attribute("total", total);
        ctx.render("cart.html");
    }

    // Adds a cupcake to the cart and goes back to the front page. On error the front page shows the message.
    private void addToCart(Context ctx) {
        int bottomId = ctx.formParamAsClass("bottom", Integer.class).get();
        int toppingId = ctx.formParamAsClass("topping", Integer.class).get();
        int quantity = ctx.formParamAsClass("quantity", Integer.class).get();

        List<Orderline> cart = ctx.sessionAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            ctx.sessionAttribute("cart", cart);
        }

        try {
            orderService.addToCart(cart, bottomId, toppingId, quantity);
        } catch (DatabaseException e) {
            ctx.sessionAttribute("error", e.getMessage());
        }
        ctx.redirect("/");
    }
}

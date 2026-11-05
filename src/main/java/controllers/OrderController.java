package controllers;

import entities.Orderline;
import entities.User;
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
        config.routes.post("/kurv/betal", ctx -> pay(ctx));
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

    // Pays for the cart. Not logged in goes to login. On error the cart is shown again with the message.
    private void pay(Context ctx) {
        User user = ctx.sessionAttribute("currentUser");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        List<Orderline> cart = ctx.sessionAttribute("cart");
        try {
            double total = orderService.calculateTotal(cart);
            int orderId = orderService.placeOrder(user, cart);

            ctx.sessionAttribute("cart", new ArrayList<Orderline>());
            ctx.attribute("orderId", orderId);
            ctx.attribute("orderlines", cart);
            ctx.attribute("total", total);
            ctx.render("confirmation.html");
        } catch (DatabaseException e) {
            ctx.attribute("error", e.getMessage());
            ctx.attribute("cart", cart);
            ctx.attribute("total", orderService.calculateTotal(cart));
            ctx.render("cart.html");
        }
    }
}

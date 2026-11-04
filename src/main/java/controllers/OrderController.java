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

    public void setRoutes(JavalinConfig config) {
        config.routes.get("/kurv",ctx -> kurv(ctx));
    }

    private void kurv(Context ctx) {
        List<Orderline> cart = ctx.sessionAttribute("cart");
        double total = orderService.calculateTotal(cart);

        ctx.attribute("cart", cart);
        ctx.attribute("total", total);
        ctx.render("kurv.html");
    }
}

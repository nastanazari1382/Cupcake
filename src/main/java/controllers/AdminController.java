package controllers;

import entities.OrderSummary;
import entities.User;
import exceptions.DatabaseException;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import persistence.ConnectionPool;
import persistence.OrderMapper;

import java.util.List;

public class AdminController {

    private final ConnectionPool connectionPool;

    public AdminController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void setRoutes(JavalinConfig config) {
        config.routes.get("/admin/ordrer", ctx -> showOrders(ctx));
    }

    private void showOrders(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.redirect("/");
            return;
        }
        try {
            List<OrderSummary> orders = OrderMapper.getAllOrders(connectionPool);
            ctx.attribute("orders", orders);
            ctx.render("adminOrders.html");
        } catch (DatabaseException e) {
            ctx.attribute("error", e.getMessage());
            ctx.render("adminOrders.html");
        }
    }

    //returns true if the user logged in is admin
    private boolean isAdmin(Context ctx) {
        User user = ctx.sessionAttribute("currentUser");
        return user != null && user.getRole().equals("admin");
    }
}

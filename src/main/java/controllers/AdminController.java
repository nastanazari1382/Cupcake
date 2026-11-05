package controllers;

import entities.OrderSummary;
import entities.User;
import exceptions.DatabaseException;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import persistence.ConnectionPool;
import persistence.OrderMapper;
import entities.CustomerOrders;
import persistence.UserMapper;

import java.util.ArrayList;

import java.util.List;

public class AdminController {

    private final ConnectionPool connectionPool;

    public AdminController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    // Registers the routes for the admin pages
    public void setRoutes(JavalinConfig config) {
        config.routes.post("/admin/ordrer/slet", ctx -> deleteOrder(ctx));
        config.routes.get("/admin/ordrer", ctx -> showOrders(ctx));
        config.routes.get("/admin/kunder", ctx -> showCustomers(ctx));
    }



    // Shows all orders to the admin.
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

    // Deletes an order and shows the orders page again
    private void deleteOrder(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.redirect("/");
            return;
        }
        try {
            int orderId = Integer.parseInt(ctx.formParam("orderId"));
            OrderMapper.deleteOrder(orderId, connectionPool);
            ctx.redirect("/admin/ordrer");
        } catch (DatabaseException e) {
            ctx.attribute("error", e.getMessage());
            showOrders(ctx);
        }
    }

    // Shows all customers and their orders to the admin
    private void showCustomers(Context ctx) {
        if (!isAdmin(ctx)) {
            ctx.redirect("/");
            return;
        }
        try {
            List<CustomerOrders> customers = new ArrayList<>();
            for (User customer : UserMapper.getAllCustomers(connectionPool)) {
                customers.add(new CustomerOrders(customer,
                        OrderMapper.getOrdersByUserId(customer.getId(), connectionPool)));
            }
            ctx.attribute("customers", customers);
            ctx.render("adminCustomers.html");
        } catch (DatabaseException e) {
            ctx.attribute("error", e.getMessage());
            ctx.render("adminCustomers.html");
        }
    }
    // Returns true if the logged-in user is admin
    private boolean isAdmin(Context ctx) {
        User user = ctx.sessionAttribute("currentUser");
        return user != null && user.getRole().equals("admin");
    }





}

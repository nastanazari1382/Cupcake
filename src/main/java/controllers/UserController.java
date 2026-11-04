package controllers;


import entities.User;
import exceptions.DatabaseException;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import persistence.ConnectionPool;
import persistence.UserMapper;

import java.util.Map;

public class UserController {
    private final ConnectionPool connectionPool;

    public UserController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    // Registers the routes for login and creating an account
    public void setRoutes(JavalinConfig config) {
        config.routes.get("/login", ctx -> ctx.render("login.html"));
        config.routes.get("/opret-konto", ctx -> ctx.render("opretKonto.html"));
        config.routes.post("/opret-konto", ctx -> createUser(ctx));
    }


    // Creates a new account, logs the user in and sends them to the cart
    private void createUser(Context ctx) {
        String email = ctx.formParam("email") == null ? "" : ctx.formParam("email").trim().toLowerCase();
        String password1 = ctx.formParam("password");
        String password2 = ctx.formParam("password2");

        if (email.isEmpty() || password1 == null || password1.isEmpty()) {
            showError(ctx, "Udfyld både email og adgangskode", email);
            return;
        }
        if (!password1.equals(password2)) {
            showError(ctx, "De to adgangskoder er ikke ens", email);
            return;
        }

        try {
            User user = UserMapper.createUser(email, password1, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            ctx.redirect("/kurv");
        } catch (DatabaseException e) {
            showError(ctx, e.getMessage(), email);
        }
    }

    // Shows the create account page again with an error message
    private void showError(Context ctx, String message, String email) {
        ctx.render("opretKonto.html", Map.of("error", message, "email", email));
    }

}
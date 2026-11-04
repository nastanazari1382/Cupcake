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
        config.routes.post("/login", ctx -> login(ctx));
        config.routes.get("/logout", ctx -> logout(ctx));
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

    // Logs the user in and saves them in the session. Shows an error if the email or password is wrong
    private void login(Context ctx) {
        String email = ctx.formParam("email") == null ? "" : ctx.formParam("email").trim().toLowerCase();
        String password = ctx.formParam("password");

        try {
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            ctx.redirect("/kurv");
        } catch (DatabaseException e) {
            ctx.attribute("error", e.getMessage());
            ctx.render("login.html");
        }
    }

    // Logs the user out by clearing the session and sends them to the front page
    private void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    // Shows the create account page again with an error message
    private void showError(Context ctx, String message, String email) {
        ctx.render("opretKonto.html", Map.of("error", message, "email", email));
    }

}
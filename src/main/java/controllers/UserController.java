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

    public void setRoutes(JavalinConfig config) {
        config.routes.get("/login", ctx -> ctx.render("login.html"));
        config.routes.get("/opret-konto", ctx -> ctx.render("opretKonto.html"));
        config.routes.post("/opret-konto", ctx -> createUser(ctx));
    }


    private void createUser(Context ctx) {
        String email = ctx.formParam("email") == null ? "" : ctx.formParam("email").trim().toLowerCase();
        String password1 = ctx.formParam("password");
        String password2 = ctx.formParam("password2");

        if (email.isEmpty() || password1 == null || password1.isEmpty()) {
            visFejl(ctx, "Udfyld både email og adgangskode", email);
            return;
        }
        if (!password1.equals(password2)) {
            visFejl(ctx, "De to adgangskoder er ikke ens", email);
            return;
        }

        try {
            User user = UserMapper.createUser(email, password1, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            ctx.redirect("/kurv");
        } catch (DatabaseException e) {
            visFejl(ctx, e.getMessage(), email);
        }
    }

    private void visFejl(Context ctx, String besked, String email) {
        ctx.render("opretKonto.html", Map.of("error", besked, "email", email));
    }

}
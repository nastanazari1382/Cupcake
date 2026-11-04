package controllers;

import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;

public class MainController {
    public void setRoutes(JavalinConfig config) {
        config.routes.get("/", ctx -> index(ctx));
        config.routes.get("/login", ctx -> login(ctx));
        config.routes.get("/opret-konto", ctx -> opretKonto(ctx));
    }

    private void index(Context ctx) {
        ctx.render("index.html");
    }


    private void login(Context ctx) {
        ctx.render("login.html");
}

    private void opretKonto(Context ctx) {
        ctx.render("opretKonto.html");
    }
}

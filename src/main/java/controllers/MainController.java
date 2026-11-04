package controllers;

import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;

public class MainController {
    public void setRoutes(JavalinConfig config) {
        config.routes.get("/", ctx -> index(ctx));
    }

    private void index(Context ctx) {
        ctx.render("index.html");
    }

}

package controllers;

import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;

public class OrderController {

    public void setRoutes(JavalinConfig config) {
        config.routes.get("/kurv",ctx -> kurv(ctx));
    }

    private void kurv(Context ctx) {
        ctx.attribute("cart", ctx.sessionAttribute("cart"));
        // TODO: ctx.attribute("total", ...) når Orderline findes
        ctx.render("kurv.html");
    }

}

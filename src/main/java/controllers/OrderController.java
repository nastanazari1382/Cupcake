package controllers;

import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class OrderController {

    public void setRoutes(JavalinConfig config) {
        config.routes.get("/kurv",ctx -> kurv(ctx));
    }

    private void kurv(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        model.put("cart", ctx.sessionAttribute("cart"));
        // TODO: model.put("total", ...) når Orderline findes
        ctx.render("kurv.html", model);
    }

}

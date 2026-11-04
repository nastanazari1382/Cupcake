package controllers;

import entities.Bottom;
import entities.Topping;
import exceptions.DatabaseException;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import persistence.BottomMapper;
import persistence.ConnectionPool;
import persistence.ToppingMapper;

import java.util.List;

public class MainController {

    private final ConnectionPool connectionPool;

    public MainController(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void setRoutes(JavalinConfig config) {
        config.routes.get("/", ctx -> index(ctx));
    }

    private void index(Context ctx) {
        try{
            List<Bottom>bottoms = BottomMapper.getAllBottoms(connectionPool);
            List<Topping>toppings = ToppingMapper.getAllToppings(connectionPool);
            ctx.attribute("bottoms", bottoms);
            ctx.attribute("toppings", toppings);
            ctx.render("index.html");
        }catch (DatabaseException e){
            ctx.attribute("error", e.getMessage());
            ctx.render("index.html");
        }
    }

}

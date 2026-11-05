import configuration.ThymeleafConfig;
import controllers.AdminController;
import controllers.MainController;
import controllers.OrderController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import persistence.ConnectionPool;
import controllers.UserController;

public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    // Starts Javalin on port 7070 and registers all controllers
    public static void main(String[] args){
        Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
            new MainController(connectionPool).setRoutes(config);
            new OrderController(connectionPool).setRoutes(config);
            new UserController(connectionPool).setRoutes(config);
            new AdminController(connectionPool).setRoutes(config);

        }).start(7070);
    }
}
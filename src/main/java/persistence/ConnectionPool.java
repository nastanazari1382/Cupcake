package persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static volatile ConnectionPool instance = null;
    private static HikariDataSource ds;

    private ConnectionPool() {
    }

    // Returns the single shared connection pool (singleton)
    public static ConnectionPool getInstance(String user, String password, String url, String db) {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {  // check again inside synchronized so two threads do not create two pools
                    if (System.getenv("DEPLOYED") != null) {
                        ds = createHikariConnectionPool(
                                System.getenv("JDBC_USER"),
                                System.getenv("JDBC_PASSWORD"),
                                System.getenv("JDBC_CONNECTION_STRING"),
                                System.getenv("JDBC_DB"));
                    } else {
                        ds = createHikariConnectionPool(user, password, url, db);
                    }
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    // Creates and configures the HikariCP pool
    private static HikariDataSource createHikariConnectionPool(String user, String password, String url, String db) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(String.format(url, db));
        config.setUsername(user);
        config.setPassword(password);

        // pool settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);      // always keep a few connections ready
        config.setIdleTimeout(30000);  // 30 seconds
        config.setConnectionTimeout(30000); // max wait time for a free connection
        config.setPoolName("Postgresql-Pool");

        // cache prepared statements so they are not rebuilt every time
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);
    }

    // Returns a connection from the pool. Not static: the pool is passed from Main to the mappers
    public Connection getConnection() throws SQLException {
        if (ds == null) {
            throw new SQLException("Connection pool is not initialized.");
        }

        return ds.getConnection();
    }
}

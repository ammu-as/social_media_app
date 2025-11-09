package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static final HikariDataSource ds;

    static {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(System.getenv().getOrDefault("MYSQL_URL",
                "jdbc:mysql://localhost:3306/social_mediaa?useSSL=false&serverTimezone=UTC"));
        cfg.setUsername(System.getenv().getOrDefault("MYSQL_USER", "root"));
        cfg.setPassword(System.getenv().getOrDefault("MYSQL_PASS", "root123"));
        cfg.setMaximumPoolSize(10);
        cfg.setPoolName("social-hybrid-pool");
        cfg.addDataSourceProperty("cachePrepStmts","true");
        cfg.addDataSourceProperty("prepStmtCacheSize","250");
        cfg.addDataSourceProperty("prepStmtCacheSqlLimit","2048");
        ds = new HikariDataSource(cfg);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void close() {
        if (ds != null && !ds.isClosed()) ds.close();
    }
}

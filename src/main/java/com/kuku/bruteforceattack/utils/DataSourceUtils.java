package com.kuku.bruteforceattack.utils;

import com.zaxxer.hikari.HikariConfig;

public class DataSourceUtils {
    public static HikariConfig getHikariConfig(String jdbUrl, String username, String password) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.setLeakDetectionThreshold(60 * 1000);
        hikariConfig.setMinimumIdle(5);
        return hikariConfig;
    }
}

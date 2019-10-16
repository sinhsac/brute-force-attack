package com.kuku.bruteforceattack.infra.config.mysql;


import com.kuku.bruteforceattack.utils.DataSourceUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Value("jdbc:mysql://${jdbc.dbhost}/${jdbc.dbname}?useSSL=false&useUnicode=true&characterEncoding=utf-8")
    String jdbUrl;
    @Value("${jdbc.username}")
    String username;
    @Value("${jdbc.password}")
    String password;
    @Value("${jdbc.poolSize.normal:3}")
    Integer normalPoolSize;
    @Value("${jdbc.poolSize.bulk:2}")
    Integer bulkPoolSize;

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = DataSourceUtils.getHikariConfig(jdbUrl, username, password);
        config.setMaximumPoolSize(normalPoolSize);

        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbc() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate namedJdbc() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

}

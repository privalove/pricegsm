package com.pricegsm.config;

import com.googlecode.flyway.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;


@Configurable
public class FlywayConfig {

    @Autowired
    private DataSource dataSource;

    @Value("${hibernate.default_schema}")
    private String defaultSchema;

    @Bean
    public Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setSchemas(defaultSchema);
        flyway.setLocations("db/migration", "filesystem:../flyway/sql");
        return flyway;
    }
}

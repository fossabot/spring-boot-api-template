package com.phoenix.api.config;

import com.phoenix.infrastructure.config.FlywayConfig;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FlywayConfiguration {
    private final FlywayConfig flywayConfig;

    public FlywayConfiguration() throws IOException {
        flywayConfig = new FlywayConfig("primary");
    }

    @Bean(name = "Flyway")
    public Flyway flyway(){
        return flywayConfig.getFlyway();
    }
}

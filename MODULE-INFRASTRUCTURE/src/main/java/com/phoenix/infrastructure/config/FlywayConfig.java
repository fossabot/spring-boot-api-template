package com.phoenix.infrastructure.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.exception.FlywayValidateException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FlywayConfig {
    private final String CONFIG_FILE = "-flyway.properties";

    public FlywayConfig() {
    }

    private Properties getProperties(String name) throws IOException {
        String configFileName = name + CONFIG_FILE;
        ClassLoader classLoader = getClass().getClassLoader();
        File configFile = new File(classLoader.getResource(configFileName).getFile());
        FileInputStream fileInputStream = new FileInputStream(configFile);

        Properties properties = new Properties();
        properties.load(fileInputStream);

        return properties;
    }

    private FluentConfiguration loadConfiguration(String name) throws IOException {
        Properties properties = getProperties(name);

        FluentConfiguration configuration = Flyway.configure()
                .dataSource(properties.getProperty("url"),
                        properties.getProperty("user"),
                        properties.getProperty("password"))
                .locations("classpath:/migration")
                .baselineOnMigrate(Boolean.parseBoolean(properties.getProperty("baselineOnMigrate")))
                .baselineVersion(properties.getProperty("baselineVersion"))
                .sqlMigrationPrefix(properties.getProperty("sqlMigrationPrefix"))
                .sqlMigrationSeparator(properties.getProperty("sqlMigrationSeparator"))
                .validateOnMigrate(Boolean.parseBoolean(properties.getProperty("validateOnMigrate")));


        return configuration;
    }

    public Flyway initializeFlyway(String name) throws IOException {
        Flyway flyway = loadConfiguration(name).load();

        return flyway;
    }

    public void migrate(Flyway flyway) {
        try {
            flyway.migrate();
        } catch (FlywayValidateException e) {
            flyway.repair();
            flyway.migrate();
        }
    }
}

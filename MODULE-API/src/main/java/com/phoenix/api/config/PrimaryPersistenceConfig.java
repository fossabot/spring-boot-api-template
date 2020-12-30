package com.phoenix.api.config;

import com.phoenix.config.PrimaryPersistenceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableJpaRepositories(basePackages = {"com.phoenix.*"},
        entityManagerFactoryRef = "PrimaryEntityManagerFactory",
        transactionManagerRef = "PrimaryTransactionManager")
@EnableTransactionManagement
public class PrimaryPersistenceConfig {
    private final PrimaryPersistenceConfiguration configuration;

    public PrimaryPersistenceConfig() throws IOException {
        configuration = new PrimaryPersistenceConfiguration();
    }

    @Primary
    @Bean(name = "PrimaryDataSource")
    public DataSource DataSource(){
        return configuration.createDataSource();
    }

    @Primary
    @Bean(name = "PrimaryLocalContainerEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean EntityManagerBean(){
        return configuration.createLocalContainerEntityManagerFactory();
    }

    @Bean(name = "PrimaryEntityManagerFactory")
    public EntityManagerFactory EntityManagerFactory(){
        return configuration.createEntityManagerFactory();
    }

    @Primary
    @Bean(name = "PrimaryTransactionManager")
    public PlatformTransactionManager TransactionManagerBean(){
        return configuration.createTransactionManagerBean();
    }

    @Bean(name = "PrimaryEntityManager")
    public EntityManager EntityManager(){
        return configuration.createEntityManager();
    }

    @Bean(name = "AuditorAware")
    public AuditorAware<String> AuditorAware(){
        return configuration.createAuditorAware();
    }
}

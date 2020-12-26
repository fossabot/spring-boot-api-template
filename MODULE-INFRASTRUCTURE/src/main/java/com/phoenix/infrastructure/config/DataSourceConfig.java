package com.phoenix.infrastructure.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

/**
 * DataSource configuration.
 * Thêm @EnableJpaRepositories(basePackages = {"com.dangdinhtai.template.*"},
 *         entityManagerFactoryRef = "primaryEntityManagerFactory",
 *         transactionManagerRef = "primaryTransactionManager")
 *
 *        để sử dụng JpaRepository
 *
 *         basePackages: scan các package có chứa repository
 *         entityManagerFactoryRef: đổi entityManager mặc định từ entityManager sang bean đc khai báo ở trong class này
 *         transactionManagerRef: đổi transactionManager mặc định từ transactionManager sang bean đc khai báo ở trong class này
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.phoenix.*"},
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "primaryTransactionManager")
@EnableTransactionManagement
@PropertySource(value = "classpath:primary-database.yaml", factory = YamlPropertySourceFactory.class)
public class DataSourceConfig {
    private final Environment environment;

    @Autowired
    public DataSourceConfig(Environment environment) {
        this.environment = environment;
    }

    @Primary
    @Bean(name = "primaryDataSource")
    public DataSource DataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(environment.getProperty(Enums.DataSourceConfigKey.JDBC_URL.value()));
        hikariConfig.setUsername(environment.getProperty(Enums.DataSourceConfigKey.USERNAME.value()));
        hikariConfig.setPassword(environment.getProperty(Enums.DataSourceConfigKey.PASSWORD.value()));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(environment.getProperty(Enums.DataSourceConfigKey.MAXIMUM_POOL_SIZE.value())));

        hikariConfig.addDataSourceProperty("cachePrepStmts", environment.getProperty(Enums.DataSourceConfigKey.CACHE_PREP_STMT.value()));
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", environment.getProperty(Enums.DataSourceConfigKey.PREP_STMT_CACHE_SIZE.value()));
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", environment.getProperty(Enums.DataSourceConfigKey.PREP_STMT_CACHE_SQL_LIMIT.value()));
        hikariConfig.addDataSourceProperty("useServerPrepStmts", environment.getProperty(Enums.DataSourceConfigKey.USE_SERVER_PREP_STMT.value()));
        hikariConfig.addDataSourceProperty("useLocalSessionState", environment.getProperty(Enums.DataSourceConfigKey.USE_LOCAL_SESSION_STATE.value()));
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", environment.getProperty(Enums.DataSourceConfigKey.REWRITE_BATCHED_STATEMENTS.value()));
        hikariConfig.addDataSourceProperty("cacheResultSetMetadata", environment.getProperty(Enums.DataSourceConfigKey.CACHE_RESULT_SET_METADATA.value()));
        hikariConfig.addDataSourceProperty("cacheServerConfiguration", environment.getProperty(Enums.DataSourceConfigKey.CACHE_SERVER_CONFIGURATION.value()));
        hikariConfig.addDataSourceProperty("elideSetAutoCommits", environment.getProperty(Enums.DataSourceConfigKey.ELIDE_SET_AUTOCOMMIT.value()));
        hikariConfig.addDataSourceProperty("maintainTimeStats", environment.getProperty(Enums.DataSourceConfigKey.MAINTAIN_TIME_STATS.value()));

        return new HikariDataSource(hikariConfig);
    }

    @Primary
    @Bean(name = "primaryEntityManager")
    public LocalContainerEntityManagerFactoryBean EntityManagerBean() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean
                = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(DataSource());

        // Scan Entities in Package:
        localContainerEntityManagerFactoryBean.setPackagesToScan(ApplicationConstant.PACKAGE_ENTITIES);
        localContainerEntityManagerFactoryBean.setPersistenceUnitName(ApplicationConstant.JPA_UNIT_NAME); // Important !!

        //
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();

        // JPA & Hibernate
        properties.put("hibernate.dialect", environment.getProperty(Enums.JpaConfigKey.HIBERNATE_DIALECT.value()));
        properties.put("hibernate.show.sql", environment.getProperty(Enums.JpaConfigKey.SHOW_SQL.value()));
        properties.put("hibernate.hbm2ddl.auto", environment.getProperty(Enums.JpaConfigKey.DLL_AUTO.value()));

        // Solved Error: PostGres createClob() is not yet implemented.
        // PostGres Only:
        // properties.put("hibernate.temp.use_jdbc_metadata_defaults",  false);

        localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);

        localContainerEntityManagerFactoryBean.afterPropertiesSet();

        return localContainerEntityManagerFactoryBean;
    }

    @Bean(name = "primaryEntityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {
        return EntityManagerBean().getObject();
    }

    @Bean(name = "primaryEntityManager")
    public EntityManager entityManager() {
        return entityManagerFactory().createEntityManager();
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager TransactionManagerBean() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(EntityManagerBean().getObject());
        return transactionManager;
    }
}

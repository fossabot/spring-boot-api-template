/*
 * MIT License
 *
 * Copyright (c) 2020 Đình Tài
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.phoenix.infrastructure.config;

import com.phoenix.infrastructure.entities.AuditorAwareImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.File;
import java.util.HashMap;

/**
 * DataSource configuration.
 * Thêm @EnableJpaRepositories(basePackages = {"com.phoenix.*"},
 * entityManagerFactoryRef = "primaryEntityManagerFactory",
 * transactionManagerRef = "primaryTransactionManager")
 * <p>
 * để sử dụng JpaRepository
 * <p>
 * basePackages: scan các package có chứa repository
 * entityManagerFactoryRef: đổi entityManager mặc định từ entityManager sang bean đc khai báo ở trong class này
 * transactionManagerRef: đổi transactionManager mặc định từ transactionManager sang bean đc khai báo ở trong class này
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.phoenix.*"},
        entityManagerFactoryRef = "PrimaryEntityManagerFactory",
        transactionManagerRef = "PrimaryTransactionManager")
@EnableTransactionManagement
@PropertySource(value = "classpath:primary-jpa.properties")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class PrimaryPersistenceConfig {

    private static final String HIKARICP_CONFIG_FILE = "primary-hikaricp.properties";
    private static final String[] PACKAGES_TO_SCAN = {"com.phoenix.*"};
    private static final String PERSISTENCE_UNIT_NAME = "PRIMARY";
    private final Environment environment;

    @Autowired
    public PrimaryPersistenceConfig(Environment environment) {
        this.environment = environment;
    }

    @Primary
    @Bean(name = "PrimaryDataSource")
    public DataSource DataSource() {
        ClassLoader classLoader = getClass().getClassLoader();
        File configFile = new File(classLoader.getResource(HIKARICP_CONFIG_FILE).getFile());

        String path = configFile.getPath();

        HikariConfig hikariConfig = new HikariConfig(path);

        return new HikariDataSource(hikariConfig);
    }

    @Primary
    @Bean(name = "PrimaryLocalContainerEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean EntityManagerBean() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean
                = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(DataSource());

        // Scan Entities in Package:
        localContainerEntityManagerFactoryBean.setPackagesToScan(PACKAGES_TO_SCAN);
        localContainerEntityManagerFactoryBean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME); // Important !!

        //
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();

        // JPA & Hibernate
        properties.put("hibernate.dialect", environment.getProperty("jpa.properties.hibernate.dialect"));
        properties.put("hibernate.show.sql", environment.getProperty("jpa.properties.hibernate.show-sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getProperty("jpa.properties.hibernate.ddl-auto"));

        // Solved Error: PostGres createClob() is not yet implemented.
        // PostGres Only:
        // properties.put("hibernate.temp.use_jdbc_metadata_defaults",  false);

        localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);

        localContainerEntityManagerFactoryBean.afterPropertiesSet();

        return localContainerEntityManagerFactoryBean;
    }

    @Bean(name = "PrimaryEntityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {
        return EntityManagerBean().getObject();
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager TransactionManagerBean() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(EntityManagerBean().getObject());
        return transactionManager;
    }

    @Bean(name = "PrimaryEntityManager")
    public EntityManager entityManager() {
        return entityManagerFactory().createEntityManager();
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
}

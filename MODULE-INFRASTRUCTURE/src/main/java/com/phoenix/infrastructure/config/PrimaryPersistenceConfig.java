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
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * DataSource configuration.
 * Thêm @EnableJpaRepositories(basePackages = {"com.phoenix.*"},
 * entityManagerFactoryRef = "PrimaryEntityManagerFactory",
 * transactionManagerRef = "PrimaryTransactionManager")
 * <p>
 * để sử dụng JpaRepository
 * <p>
 * basePackages: scan các package có chứa repository
 * entityManagerFactoryRef: đổi entityManager mặc định từ entityManager sang bean đc khai báo ở trong class này
 * transactionManagerRef: đổi transactionManager mặc định từ transactionManager sang bean đc khai báo ở trong class này
 */
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class PrimaryPersistenceConfig {

    private static final String HIKARICP_CONFIG_FILE = "primary-hikaricp.properties";
    private static final String JPA_CONFIG_FILE = "primary-jpa.properties";
    private static final String[] PACKAGES_TO_SCAN = {"com.phoenix.*"};

    public PrimaryPersistenceConfig() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File configFile = new File(classLoader.getResource(JPA_CONFIG_FILE).getFile());

        FileInputStream fileInputStream = new FileInputStream(configFile);

        Properties jpaProperties = new Properties();

        jpaProperties.load(fileInputStream);
    }

    public DataSource createDataSource() {
        ClassLoader classLoader = getClass().getClassLoader();
        File configFile = new File(classLoader.getResource(HIKARICP_CONFIG_FILE).getFile());

        String path = configFile.getPath();

        HikariConfig hikariConfig = new HikariConfig(path);

        return new HikariDataSource(hikariConfig);
    }

    public LocalContainerEntityManagerFactoryBean createLocalContainerEntityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean
                = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource);

        // Scan Entities in Package:
        localContainerEntityManagerFactoryBean.setPackagesToScan(PACKAGES_TO_SCAN);
        //localContainerEntityManagerFactoryBean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME); // Important !!
        localContainerEntityManagerFactoryBean.setPersistenceUnitName(PersistenceUnitsName.PRIMARY_PERSISTENCE_UNIT_NAME); // Important !!

        //
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();

        // JPA & Hibernate
        properties.put("hibernate.dialect", properties.get("jpa.properties.hibernate.dialect"));
        properties.put("hibernate.show.sql", properties.get("jpa.properties.hibernate.show-sql"));
        properties.put("hibernate.hbm2ddl.auto", properties.get("jpa.properties.hibernate.ddl-auto"));
        properties.put("jpa.properties.hibernate.format_sql", properties.get("jpa.properties.hibernate.format_sql"));
        properties.put("logging.level.org.hibernate.SQL", properties.get("logging.level.org.hibernate.SQL"));

        // Solved Error: PostGres createClob() is not yet implemented.
        // PostGres Only:
        // properties.put("hibernate.temp.use_jdbc_metadata_defaults",  false);

        localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);

        localContainerEntityManagerFactoryBean.afterPropertiesSet();

        return localContainerEntityManagerFactoryBean;
    }

    public EntityManagerFactory createEntityManagerFactory(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        return localContainerEntityManagerFactoryBean.getObject();
    }

    public PlatformTransactionManager createTransactionManagerBean(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    public EntityManager createEntityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    public AuditorAware<String> createAuditorAware() {
        return new AuditorAwareImpl();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agile.configuration;

/**
 *
 * @author jainab
 */
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.agile.repository",
        entityManagerFactoryRef = "agileEntityManagerFactory",
        transactionManagerRef = "agileTransactionManager"
)
@PropertySource("classpath:database.properties")
//@PropertySource("file:////home/core/agile/database.properties")
public class AgileManagementDataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.agile")
    public DataSourceProperties agileDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.agile.configuration")
    public DataSource agileDataSource() {
        return agileDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "agileEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean agileEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(agileDataSource())
                .packages(new String[]{"com.agile.model"})
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager agileTransactionManager(
            final @Qualifier("agileEntityManagerFactory") LocalContainerEntityManagerFactoryBean agileEntityManagerFactory) {
        return new JpaTransactionManager(agileEntityManagerFactory.getObject());
    }

}

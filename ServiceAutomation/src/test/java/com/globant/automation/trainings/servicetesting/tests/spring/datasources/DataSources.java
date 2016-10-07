package com.globant.automation.trainings.servicetesting.tests.spring.datasources;

/**
 * Created by juan on 9/19/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

//@EntityScan(basePackageClasses = BaseEntity.class)
@Configuration
public class DataSources {

//    @EnableJpaRepositories(basePackageClasses = UsersRepository.class)
    @Configuration
    public static class UsersDataSource {
        @Autowired
        Environment env;

        @Bean
        public DataSource getDataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl(env.getRequiredProperty("db.users.url"));
            dataSource.setUsername(env.getRequiredProperty("db.users.user"));
            dataSource.setPassword(env.getRequiredProperty("db.users.password"));
            return dataSource;
        }
    }

//    @EnableJpaRepositories(basePackageClasses = ServiceRepository.class)
    @Configuration
    public static class SomethingDataSource {
        @Autowired
        Environment env;

        @Bean
        public DataSource getDataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl(env.getRequiredProperty("db.something.url"));
            dataSource.setUsername(env.getRequiredProperty("db.something.user"));
            dataSource.setPassword(env.getRequiredProperty("db.something.password"));
            return dataSource;
        }
    }
}
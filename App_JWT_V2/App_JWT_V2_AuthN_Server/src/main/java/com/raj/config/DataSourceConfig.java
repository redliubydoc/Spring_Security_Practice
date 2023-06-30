package com.raj.config;

import oracle.jdbc.datasource.impl.OracleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;


@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource getDataSource() throws SQLException {

        DataSource dataSource = new OracleDataSource();

        ((OracleDataSource) dataSource).setURL("jdbc:oracle:thin:@localhost:1521/xepdb1");
        ((OracleDataSource) dataSource).setUser("practice");
        ((OracleDataSource) dataSource).setPassword("@@hack");

        return dataSource;
    }
}

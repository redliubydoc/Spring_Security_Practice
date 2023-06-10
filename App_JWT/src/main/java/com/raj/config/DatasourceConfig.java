package com.raj.config;

import oracle.jdbc.pool.OracleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DatasourceConfig {

    @Bean
    public DataSource getDateSource() throws SQLException {

        DataSource dataSource = new OracleDataSource();

        ((OracleDataSource) dataSource).setURL("jdbc:oracle:thin:@localhost:1521:xe");
        ((OracleDataSource) dataSource).setUser("system");
        ((OracleDataSource) dataSource).setPassword("@@hack");

        return dataSource;
    }
}

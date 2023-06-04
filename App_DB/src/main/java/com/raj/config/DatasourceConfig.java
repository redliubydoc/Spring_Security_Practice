package com.raj.config;

import oracle.jdbc.pool.OracleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DatasourceConfig {

    @Bean
    public DataSource getDateSource() {

        DataSource dataSource = null;

        try {
            dataSource = new OracleDataSource();
            ((OracleDataSource) dataSource).setURL("jdbc:oracle:thin:@localhost:1521:xe");
            ((OracleDataSource) dataSource).setUser("system");
            ((OracleDataSource) dataSource).setPassword("@@hack");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return dataSource;
    }
}

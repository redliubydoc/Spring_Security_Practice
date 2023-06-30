//package com.raj;
//
//import oracle.sql.ARRAY;
//import oracle.sql.ArrayDescriptor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.sql.DataSource;
//import java.sql.*;
//
//@SpringBootTest
//public
//class AppJwtV2ResourceServerApplicationTests {
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Test
//    public void contextLoads() throws SQLException {
//        try (Connection connection = dataSource.getConnection()) {
//            ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("type_varr_nums", connection);
//            CallableStatement cStmt = connection.prepareCall("{ call proc_test(?)}");
//            cStmt.setArray(1, new ARRAY(descriptor , connection, new int[] {1, 2, 3}));
//            cStmt.execute();
//        }
//    }
//
//}

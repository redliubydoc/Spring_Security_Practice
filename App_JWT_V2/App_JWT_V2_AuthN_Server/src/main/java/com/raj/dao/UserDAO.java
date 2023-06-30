package com.raj.dao;

import com.raj.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


@Component
public class UserDAO  {
    private final DataSource dataSource;

    @Autowired
    public UserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<User> getUserByUserId(String userId) {

        User user = null;

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement pStmt = connection.prepareStatement(
                "select distinct\n" +
                "    tuser.user_id,\n" +
                "    tuser.password,\n" +
                "    trole.role_nm\n" +
                "from\n" +
                "    tuser,\n" +
                "    trole,\n" +
                "    tuser_role\n" +
                "where\n" +
                "    tuser.user_id = tuser_role.user_id and\n" +
                "    trole.role_id = tuser_role.role_id and\n" +
                "    tuser.user_id = ?"
            );
            pStmt.setString(1, userId);

            ResultSet result = pStmt.executeQuery();
            if (result.next()) {
                user = new User(
                    result.getString("user_id"),
                    result.getString("password"),
                    result.getString("role_nm")
                );
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }
}

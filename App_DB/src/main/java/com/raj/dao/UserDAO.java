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
public class UserDAO {

    @Autowired
    DataSource dataSource;

    public Optional<User> getUserByUserId(String userId) {

        User user;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement pStmt = connection.prepareStatement(
                    "select distinct\n" +
                    "    tuser.user_id,\n" +
                    "    tuser.password,\n" +
                    "    trole.role_nm\n" +
                    "from\n" +
                    "    sys.tuser,\n" +
                    "    sys.trole,\n" +
                    "    sys.tuser_role\n" +
                    "where\n" +
                    "    tuser.user_id = tuser_role.user_id and\n" +
                    "    trole.role_id = tuser_role.role_id and\n" +
                    "    tuser.user_id = ?"
            );
            pStmt.setString(1, userId);

            ResultSet result = pStmt.executeQuery();
            result.next();

            user = new User(
                    result.getString("user_id"),
                    result.getString("password"),
                    result.getString("role_nm")
            );
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(user);
    }
}

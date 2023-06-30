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
                "select\n" +
                "    user_id,\n" +
                "    firstname,\n" +
                "    lastname,\n" +
                "    email_id\n" +
                "from\n" +
                "    tuser\n" +
                "where\n" +
                "    user_id = ?"
            );
            pStmt.setString(1, userId);

            ResultSet result = pStmt.executeQuery();
            if (result.next()) {
                user = new User(
                    result.getString("user_id"),
                    result.getString("firstname"),
                    result.getString("lastname"),
                    result.getString("email_id")
                );
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }
}


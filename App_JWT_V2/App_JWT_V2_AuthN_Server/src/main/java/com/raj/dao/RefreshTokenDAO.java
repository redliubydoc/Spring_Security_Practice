package com.raj.dao;

import com.raj.AppConstants;
import com.raj.model.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Component
public class RefreshTokenDAO {
    private final DataSource dataSource;

    @Autowired
    public RefreshTokenDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean addToken(RefreshToken refreshToken) {

        boolean added = false;

        try (Connection connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);

            try {
                CallableStatement cStmt = connection.prepareCall("{ call proc_add_token(?, ?, ?, ?)}");
                cStmt.setString(1, refreshToken.getToken());
                cStmt.setString(2, refreshToken.getUserId());
                cStmt.setTimestamp(3, new Timestamp(refreshToken.getExpiresAt().getTime()));
                cStmt.setInt(4, AppConstants.MAX_CONCURRENT_LOGIN_PER_USER);
                cStmt.execute();
            }
            catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
            finally {
                connection.commit();
                added = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return added;
    }

    public Optional<RefreshToken> findByToken(String token) {

        RefreshToken refreshToken = null;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pStmt = connection.prepareStatement(
                "select\n" +
                "    token,\n" +
                "    user_id,\n" +
                "    expires_at\n" +
                "from\n" +
                "    trefresh_token\n" +
                "where\n" +
                "    token = ?"
            );
            pStmt.setString(1, token);

            ResultSet result = pStmt.executeQuery();

            if (result.next()) {
                refreshToken = new RefreshToken(
                    result.getString("token"),
                    result.getString("user_id"),
                    result.getDate("expires_at")
                );
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(refreshToken);
    }

    public boolean deleteToken(RefreshToken refreshToken) {

        boolean deleted = false;

        try (Connection connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);

            try {
                PreparedStatement pStmt = connection.prepareStatement(
                    "delete from\n" +
                    "    trefresh_token\n" +
                    "where\n" +
                    "    token = ?"
                );
                pStmt.setString(1, refreshToken.getToken());
                pStmt.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
            finally {
                connection.commit();
                deleted = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return deleted;
    }

    public boolean deleteAllExpired() {

        boolean deletedAll = false;

        try (Connection connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);

            try {
                PreparedStatement pStmt = connection.prepareStatement(
                    "delete from\n" +
                    "    trefresh_token\n" +
                    "where\n" +
                    "    expires_at <= ?"
                );
                pStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                pStmt.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
            finally {
                connection.commit();
                deletedAll = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return  deletedAll;
    }
}
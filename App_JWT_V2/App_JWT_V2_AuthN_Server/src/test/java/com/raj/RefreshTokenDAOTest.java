package com.raj;

import com.raj.dao.RefreshTokenDAO;
import com.raj.model.RefreshToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.Date;

@SpringBootTest
public class RefreshTokenDAOTest {
    private final RefreshTokenDAO refreshTokenDAO;

    @Autowired
    public RefreshTokenDAOTest(RefreshTokenDAO refreshTokenDAO) {
        this.refreshTokenDAO = refreshTokenDAO;
    }

    @Test
    public void findTokenCountByUserIdTest() throws SQLException {
        String userId = "user_001";
        int count = refreshTokenDAO.findTokenCountByUserId(userId);
        System.out.println("[" + userId + "] => " + count);
    }

    @Test
    public void addTest() throws SQLException {
        refreshTokenDAO.addToken(new RefreshToken(
            "token_011",
            "user_002",
            new Date(System.currentTimeMillis()))
        );
    }
}

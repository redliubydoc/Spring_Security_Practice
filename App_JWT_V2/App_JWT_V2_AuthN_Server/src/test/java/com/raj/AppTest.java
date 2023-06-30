package com.raj;

import com.raj.dao.RefreshTokenDAO;
import com.raj.dao.UserDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DAOTest {
    private final UserDAO userDAO;
    private final RefreshTokenDAO refreshTokenDAO;

    @Autowired
    public DAOTest(UserDAO userDAO, RefreshTokenDAO refreshTokenDAO) {
        this.userDAO = userDAO;
        this.refreshTokenDAO = refreshTokenDAO;
    }

    @Test
    void getUserByUserIdTest() {
        System.out.println(userDAO.getUserByUserId("user_001"));
    }

    @Test
    void getRefreshTokenByUserIdAndTokenTest() {
        System.out.println(refreshTokenDAO.findByUserIdAndToken("user_001", "token_001"));
    }
}

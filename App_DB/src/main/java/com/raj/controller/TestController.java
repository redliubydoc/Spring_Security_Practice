package com.raj.controller;

import com.raj.dao.UserDAO;
import com.raj.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/private")
public class TestController {
    private final UserDAO userDAO;

    @Autowired
    public TestController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping(path = "/test")
    public User test() {
        return userDAO.getUserByUserId("user_001").get();
    }
}

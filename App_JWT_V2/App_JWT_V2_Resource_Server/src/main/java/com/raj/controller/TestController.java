package com.raj.controller;

import com.raj.dao.UserDAO;
import com.raj.dto.GenericResponseDTO;
import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private final UserDAO userDAO;

    public TestController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping(path = "/admin/details")
    public ResponseEntity<GenericResponseDTO> getAdminDetails() {
        String userId = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(200).body(
            new GenericResponseDTO(userDAO.getUserByUserId(userId).get(), false, null)
        );
    }

    @GetMapping(path = "/user/details")
    public ResponseEntity<GenericResponseDTO> getUserDetails() {
        String userId = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(200).body(
                new GenericResponseDTO(userDAO.getUserByUserId(userId).get(), false, null)
        );
    }
}

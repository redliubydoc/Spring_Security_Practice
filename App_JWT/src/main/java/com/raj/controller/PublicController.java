package com.raj.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/public")
public class PublicController {

    @GetMapping(path = "/ep1")
    public ResponseEntity<Map<String, Object>> endPoint1() {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("endpoint", "endpoint @ public");
        responseBody.put("authentication", SecurityContextHolder.getContext().getAuthentication().toString());

        return ResponseEntity.ok().body(responseBody);
    }
}

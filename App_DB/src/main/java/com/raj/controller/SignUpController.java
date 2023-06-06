package com.raj.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/public")
public class SignUpController {

    @PostMapping(path = "/signup")
    public String signUp() {
        return "signup end point";
    }
}

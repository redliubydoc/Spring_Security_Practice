package com.raj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/a")
public class AuthNController {

    @GetMapping(path = "/b")
    public String doLogin() {
        return "ok";
    }
}

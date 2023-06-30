package com.raj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AppAuthNServer {
    public static void main(String[] args) {
        SpringApplication.run(AppAuthNServer.class, args);
    }
}

package com.example.userservice.controller;

import org.springframework.core.SpringVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/users/health")
    public String health() {
      System.out.println("Spring Version: " + SpringVersion.getVersion());
        return "User Service is running!";
    }
}
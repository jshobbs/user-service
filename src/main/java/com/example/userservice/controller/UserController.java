package com.example.userservice.controller;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/health")
    public String health() {

        logger.debug("Health check request received.");

        return "User Service is running!";
    }


    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest userRequest) {

        logger.info("POST /users request received.");

        UserResponse response =
                userService.createUser(userRequest);

        logger.info(
                "POST /users completed. Created user ID {}.",
                response.getId()
        );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }


    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        logger.info("GET /users request received.");

        List<UserResponse> users =
                userService.getAllUsers();

        logger.info(
                "GET /users completed. Returning {} user(s).",
                users.size()
        );

        return ResponseEntity.ok(users);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        logger.info(
                "GET /users/{} request received.",
                id
        );

        UserResponse response =
                userService.getUserById(id);

        logger.debug(
                "Returning user ID {}.",
                response.getId()
        );

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest) {

        logger.info(
                "PUT /users/{} request received.",
                id
        );

        UserResponse response =
                userService.updateUser(id, userRequest);

        logger.info(
                "PUT /users/{} completed successfully.",
                id
        );

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        logger.info(
                "DELETE /users/{} request received.",
                id
        );

        userService.deleteUser(id);

        logger.info(
                "DELETE /users/{} completed successfully.",
                id
        );

        return ResponseEntity.noContent().build();
    }

}
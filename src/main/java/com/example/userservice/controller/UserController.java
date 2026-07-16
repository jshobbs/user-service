package com.example.userservice.controller;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Controller", description = "Operations for managing users")
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "Get service health status")
    @GetMapping("/health")
    public String health() {

        logger.debug("Health check request received.");

        return "User Service is running!";
    }

    @Operation(summary = "Create a new user")
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


    @Operation(summary = "Get list of all users")
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


    @Operation(summary = "Get user by ID")
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

    @Operation(summary = "Update an existing user by ID")
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


    @Operation(summary = "Delete a user by ID")
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
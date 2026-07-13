package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;

import java.util.List;

public interface UserService {

    /**
     * Create a new user.
     *
     * @param userRequest User information
     * @return Created user
     */
    UserResponse createUser(UserRequest userRequest);

    /**
     * Retrieve all users.
     *
     * @return List of users
     */
    List<UserResponse> getAllUsers();

    /**
     * Retrieve a user by ID.
     *
     * @param id User ID
     * @return User
     */
    UserResponse getUserById(Long id);

    /**
     * Update an existing user.
     *
     * @param id User ID
     * @param userRequest Updated information
     * @return Updated user
     */
    UserResponse updateUser(Long id, UserRequest userRequest);

    /**
     * Delete a user.
     *
     * @param id User ID
     */
    void deleteUser(Long id);

}
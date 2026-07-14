package com.example.userservice.service.impl;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {

        logger.info("Creating user with email '{}'.", userRequest.getEmail());

        User user = new User();

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());

        logger.debug("Saving new user to the database.");

        User savedUser = userRepository.save(user);

        logger.info("User created successfully with ID {}.", savedUser.getId());

        return mapToResponse(savedUser);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        logger.info("Retrieving all users.");

        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        logger.info("Retrieved {} user(s).", users.size());

        return users;
    }

    @Override
    public UserResponse getUserById(Long id) {

        logger.info("Retrieving user with ID {}.", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User with ID {} was not found.", id);
                    return new ResourceNotFoundException(
                            "User not found with id: " + id);
                });

        logger.debug("User with ID {} retrieved successfully.", id);

        return mapToResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {

        logger.info("Updating user with ID {}.", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User with ID {} was not found.", id);
                    return new ResourceNotFoundException(
                            "User not found with id: " + id);
                });

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());

        logger.debug("Saving updated user with ID {}.", id);

        User updatedUser = userRepository.save(user);

        logger.info("User with ID {} updated successfully.", id);

        return mapToResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        logger.info("Deleting user with ID {}.", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User with ID {} was not found.", id);
                    return new ResourceNotFoundException(
                            "User not found with id: " + id);
                });

        userRepository.delete(user);

        logger.info("User with ID {} deleted successfully.", id);
    }

    /**
     * Converts a User entity into a UserResponse DTO.
     */
    private UserResponse mapToResponse(User user) {

        logger.debug("Mapping User entity with ID {} to UserResponse DTO.",
                user.getId());

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

}
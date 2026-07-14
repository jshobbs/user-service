package com.example.userservice.exception;

import com.example.userservice.dto.ErrorResponse;
import com.example.userservice.dto.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * Handles users that cannot be found in the database.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        logger.warn(
                "Resource not found: {}",
                ex.getMessage()
        );

        ErrorResponse response = new ErrorResponse();

        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    /**
     * Handles validation failures from @Valid annotations.
     *
     * Example:
     * Missing firstName
     * Missing lastName
     * Invalid email format
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        logger.warn(
                "Validation failed for request: {}",
                request.getRequestURI()
        );

        List<ValidationError> validationErrors = new ArrayList<>();

        for (FieldError fieldError :
                ex.getBindingResult().getFieldErrors()) {

            logger.debug(
                    "Validation error - field: {}, message: {}",
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );

            validationErrors.add(
                    new ValidationError(
                            fieldError.getField(),
                            fieldError.getDefaultMessage()
                    )
            );
        }

        ErrorResponse response = new ErrorResponse();

        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError("Validation Failed");
        response.setMessage(
                "One or more validation errors occurred."
        );
        response.setPath(request.getRequestURI());
        response.setFieldErrors(validationErrors);

        return ResponseEntity
                .badRequest()
                .body(response);
    }


    /**
     * Catch-all handler for unexpected errors.
     *
     * Internal details are logged but not exposed
     * to API consumers.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        logger.error(
                "Unhandled exception processing request {}",
                request.getRequestURI(),
                ex
        );

        ErrorResponse response = new ErrorResponse();

        response.setTimestamp(LocalDateTime.now());
        response.setStatus(
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        response.setError(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
        );
        response.setMessage(
                "An unexpected error occurred."
        );
        response.setPath(
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
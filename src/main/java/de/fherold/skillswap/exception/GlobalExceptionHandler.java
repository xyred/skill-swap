package de.fherold.skillswap.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogicError(BusinessRuleException ex) {
        log.warn("Business rule violation: {}", ex.getMessage());
        String code = (ex.getErrorCode() != null) ? ex.getErrorCode() : "BUSINESS_RULE_VIOLATION";
        return buildErrorResponse(ex.getMessage(), code, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Validation failed: {}", message);

        return buildErrorResponse(message, "VALIDATION_FAILED", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        return buildErrorResponse(ex.getMessage(), "VALIDATION_FAILED", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex) {
        log.warn("Path not found: {}", ex.getResourcePath());

        return buildErrorResponse(
                "The URL path '" + ex.getResourcePath() + "' does not exist.",
                "PATH_NOT_FOUND",
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Unexpected error occurred: ", ex);
        return buildErrorResponse("An unexpected error occurred", "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return buildErrorResponse(
                "Access denied: You do not have the required permissions.",
                "ACCESS_DENIED",
                HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, String code, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                message,
                code);
        return new ResponseEntity<>(error, status);
    }
}

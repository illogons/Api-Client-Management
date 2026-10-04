package com.gonzalovega.clientmanagement.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.aspectj.weaver.bcel.asm.AsmDetector.rootCause;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Helper method to build a consistent error response.
     *
     * @param messageKey the key for the error message in the message source
     * @param status the HTTP status to return
     * @param locale the locale for message localization
     * @param details additional details about the error (optional)
     * @return a ResponseEntity containing the ErrorResponse
     */
    private ResponseEntity<ErrorResponse> buildResponse(String messageKey, HttpStatus status, Locale locale, Map<String, String> details) {

        // Fetch the localized message using the message key and locale
        String message = messageSource.getMessage(messageKey, null, messageKey, locale);
        // Create an ErrorResponse object with the status, message, timestamp, and details
        ErrorResponse error = new ErrorResponse(status.value(), message, LocalDateTime.now(), details);
        return new ResponseEntity<>(error, status);
    }

    /**
     * Handles validation errors when method arguments fail validation constraints.
     *
     * @param ex the exception containing validation errors
     * @param locale the locale for message localization
     * @return a ResponseEntity containing the ErrorResponse with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, Locale locale) {

        Map<String, String> details = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> messageSource.getMessage(error, locale),
                        (existing, replacement) -> existing
                ));

        log.warn("Validation failed for object '{}'. Errors: {}", ex.getBindingResult().getObjectName(), details);
        return buildResponse("error.validation.failed", HttpStatus.BAD_REQUEST, locale, details);
    }

    /**
     * Handles database integrity violations, such as unique constraint violations.
     *
     * @param ex the exception indicating a data integrity violation
     * @param locale the locale for message localization
     * @return a ResponseEntity containing the ErrorResponse with a conflict status
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, Locale locale) {

        String rootCause = ex.getRootCause() != null ? ex.getRootCause().getMessage() : "Unknown Root Cause";

        log.error("Database Integrity Violation: {}. Full stack trace: ", rootCause, ex);

        return buildResponse("error.database.integrity", HttpStatus.CONFLICT, locale, null);
    }

    /**
     * Handles optimistic locking failures, which occur when concurrent updates conflict.
     *
     * @param ex the exception indicating an optimistic locking failure
     * @param locale the locale for message localization
     * @return a ResponseEntity containing the ErrorResponse with a conflict status
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLocking(ObjectOptimisticLockingFailureException ex, Locale locale) {

        log.warn("Optimistic locking failure for entity {} with ID {}: {}", ex.getPersistentClassName(), ex.getIdentifier(), ex.getMessage());
        return buildResponse("error.database.conflict", HttpStatus.CONFLICT, locale, null);
    }

    /**
     * Handles generic runtime exceptions that are not caught by more specific handlers.
     *
     * @param ex the runtime exception that occurred
     * @param locale the locale for message localization
     * @return a ResponseEntity containing the ErrorResponse with a bad request status
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, Locale locale) {

        log.error(ex.getMessage(), ex);
        return buildResponse("error.runtime.generic", HttpStatus.BAD_REQUEST, locale, Map.of("details", ex.getMessage()));
    }

    /**
     * Handles all other exceptions that are not caught by specific handlers, returning a generic internal server error response.
     *
     * @param ex the exception that occurred
     * @param locale the locale for message localization
     * @return a ResponseEntity containing the ErrorResponse with an internal server error status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, Locale locale) {

        String errorId = UUID.randomUUID().toString();

        log.error("UNEXPECTED ERROR [ID: {}]: {} - Trace: ", errorId, ex.getMessage(), ex);

        Map<String, String> details = Map.of("errorId", errorId);
        return buildResponse("error.internal.server", HttpStatus.INTERNAL_SERVER_ERROR, locale, details);
    }
}
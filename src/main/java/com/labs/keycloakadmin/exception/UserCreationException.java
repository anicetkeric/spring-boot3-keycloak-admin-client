package com.labs.keycloakadmin.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * An exception that is thrown when a Keycloak user error is detected.
 */
@ResponseStatus(CONFLICT)
public class UserCreationException extends RuntimeException {

    public UserCreationException(String message) {
        super(message);
    }
}
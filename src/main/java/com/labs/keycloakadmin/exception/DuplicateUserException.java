package com.labs.keycloakadmin.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * An exception that is thrown when a duplicate Keycloak user is detected.
 */
@ResponseStatus(CONFLICT)
public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException(String username) {
        super("Duplicate user: " + username);
    }
}
package com.labs.keycloakadmin.dto;


import lombok.Builder;

@Builder
public record UserDto(String id, String username, String email, String firstName, String lastName, String password) {
}
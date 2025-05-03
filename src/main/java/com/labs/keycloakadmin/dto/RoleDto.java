package com.labs.keycloakadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoleDto(@NotNull @NotBlank String name, String description) {
}

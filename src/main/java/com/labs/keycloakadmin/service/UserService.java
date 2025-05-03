package com.labs.keycloakadmin.service;

import com.labs.keycloakadmin.dto.UserDto;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    int count();

    List<UserDto> getAll();

    List<UserDto> getByUsername(String username);

    UserDto getById(String id);

    void assignToGroup(String userId, String groupId);

    void assignRole(String userId, String roleName);
}

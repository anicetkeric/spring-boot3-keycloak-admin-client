package com.labs.keycloakadmin.service.impl;

import com.labs.keycloakadmin.config.KeycloakPropertiesConfig;
import com.labs.keycloakadmin.dto.UserDto;
import com.labs.keycloakadmin.exception.DuplicateUserException;
import com.labs.keycloakadmin.exception.UserCreationException;
import com.labs.keycloakadmin.service.UserService;
import jakarta.ws.rs.ProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.stereotype.Service;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import jakarta.ws.rs.core.Response;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final Keycloak keycloak;
    private final KeycloakPropertiesConfig propertiesConfig;

    public UserServiceImpl(Keycloak keycloak, KeycloakPropertiesConfig propertiesConfig) {
        this.keycloak = keycloak;
        this.propertiesConfig = propertiesConfig;
    }

    private UsersResource usersResourceInstance() {
        return keycloak.realm(propertiesConfig.getRealm()).users();
    }

    private UserDto mapToUserDto(UserRepresentation userRepresentation) {
        return UserDto.builder()
                .id(userRepresentation.getId())
                .username(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .build();
    }

    @Override
    public int count() {
        return usersResourceInstance().count();
    }

    @Override
    public List<UserDto> getAll() {
        return usersResourceInstance().list().stream().map(this::mapToUserDto).toList();
    }

    @Override
    public List<UserDto> getByUsername(String username) {
        return usersResourceInstance()
                .search(username).stream().map(this::mapToUserDto).toList();
    }

    @Override
    public UserDto getById(String id) {
        var user = usersResourceInstance()
                .get(id)
                .toRepresentation();

        return mapToUserDto(user);
    }

    @Override
    public void assignToGroup(String userId, String groupId) {
        usersResourceInstance()
                .get(userId)
                .joinGroup(groupId);
    }

    @Override
    public void assignRole(String userId, String roleName) {
        var roleRepresentation = keycloak.realm(propertiesConfig.getRealm()).roles().get(roleName).toRepresentation();
        usersResourceInstance()
                .get(userId)
                .roles()
                .realmLevel()
                .add(Collections.singletonList(roleRepresentation));
    }

    @Override
    public UserDto createUser(UserDto userDto){
        var user = buildUserRepresentation(userDto);

        try (Response response = usersResourceInstance().create(user)) {
            int statusCode = response.getStatus();
            switch (statusCode) {
                case 201 -> log.info("User {} successfully created in Keycloak", userDto.username());
                case 409 -> {
                    log.error("Duplicate user {}", userDto.username());
                    throw new DuplicateUserException(userDto.username());
                }
                default -> {
                    log.error("Error creating user: status code {}", statusCode);
                    throw new UserCreationException(MessageFormat.format("Error creating user: status code {0}", statusCode));
                }
            }
        } catch (ProcessingException e) {
            log.error("Error creating user in Keycloak", e);
            throw new UserCreationException("Error creating user");
        }

        return userDto;
    }

    private UserRepresentation buildUserRepresentation(UserDto userDto) {
        UserRepresentation userRepresentation  = new UserRepresentation();
        userRepresentation.setUsername(userDto.username());
        userRepresentation.setCredentials(Collections.singletonList(buildCredentialRepresentation(userDto.password())));
        userRepresentation.setEnabled(true);
        userRepresentation.setEmail(userDto.email());
        userRepresentation.setFirstName(userDto.firstName());
        userRepresentation.setLastName(userDto.lastName());
        userRepresentation.setEmailVerified(true);

        return userRepresentation ;
    }

    /**
     *  Define password credential
     * @param password dto password
     * @return CredentialRepresentation
     */
    private CredentialRepresentation buildCredentialRepresentation(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        return credentialRepresentation;
    }
}
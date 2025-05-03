package com.labs.keycloakadmin.service.impl;

import com.labs.keycloakadmin.config.KeycloakPropertiesConfig;
import com.labs.keycloakadmin.dto.RoleDto;
import com.labs.keycloakadmin.service.RoleService;
import org.keycloak.admin.client.resource.RolesResource;
import org.springframework.stereotype.Service;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final Keycloak keycloak;
    private final KeycloakPropertiesConfig propertiesConfig;

    public RoleServiceImpl(Keycloak keycloak, KeycloakPropertiesConfig propertiesConfig) {
        this.keycloak = keycloak;
        this.propertiesConfig = propertiesConfig;
    }

    private RolesResource rolesResourceInstance() {
        return keycloak.realm(propertiesConfig.getRealm()).roles();
    }

    @Override
    public void create(RoleDto roleDto) {
        RoleRepresentation role = new RoleRepresentation();
        role.setName(roleDto.name());
        role.setDescription(roleDto.description());

        rolesResourceInstance().create(role);
    }

    @Override
    public List<RoleDto> getAll() {
        var roleRepresentations = rolesResourceInstance().list();

        return roleRepresentations.stream().map(r -> new RoleDto(r.getName(), r.getDescription())).toList();
    }

    @Override
    public RoleDto getByName(String roleName) {
        var roleRepresentation = rolesResourceInstance().get(roleName).toRepresentation();

        return new RoleDto(roleRepresentation.getName(), roleRepresentation.getDescription());
    }
}
package com.labs.keycloakadmin.service;

import com.labs.keycloakadmin.dto.RoleDto;

import java.util.List;

public interface RoleService {

    void create(RoleDto roleDto);

    List<RoleDto> getAll();

    RoleDto getByName(String roleName);
}

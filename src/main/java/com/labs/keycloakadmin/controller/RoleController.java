package com.labs.keycloakadmin.controller;

import com.labs.keycloakadmin.dto.RoleDto;
import com.labs.keycloakadmin.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> findAll() {
        var roles = roleService.getAll();
        return ResponseEntity.ok(roles);
    }

    @PostMapping
    public ResponseEntity<Void> createRole(@RequestBody RoleDto roleDto) {
        roleService.create(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
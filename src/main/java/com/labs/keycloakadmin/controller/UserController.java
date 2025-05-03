package com.labs.keycloakadmin.controller;

import com.labs.keycloakadmin.dto.UserDto;
import com.labs.keycloakadmin.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        var users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{userId}/assign/role/{roleName}")
    public ResponseEntity<?>  assignRoleToUser(@PathVariable String userId, @PathVariable String roleName){

        userService.assignRole(userId, roleName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spring.OAuth2.controller;

import com.spring.OAuth2.controller.dto.CreateUserDto;
import com.spring.OAuth2.entities.Role;
import com.spring.OAuth2.entities.User;
import com.spring.OAuth2.repository.RoleRepository;
import com.spring.OAuth2.repository.UserRepository;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Dan
 */
@RestController
public class UserController {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public UserController(UserRepository userRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {
        
        var basicRole = roleRepository.findByName(Role.Values.USER.name());
        
        var userFromDb = userRepository.findByUserEmail(dto.userEmail());
        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        
        var user = new User();
        user.setUsername(dto.userName());
        user.setUserEmail(dto.userEmail());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));
        
        userRepository.save(user);
        
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/users2")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<List<User>> listUsersTwo() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}

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
 * Controlador responsável pelo gerenciamento de usuários.
 *
 * <p>
 * Esta classe contém endpoints para criar novos usuários e listar os usuários
 * existentes no sistema. O acesso às operações é controlado por permissões
 * específicas.</p>
 *
 * <p>
 * Os usuários criados recebem automaticamente a role básica de "USER".</p>
 *
 * @author danrleybrasil
 */
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Construtor da classe UserController.
     *
     * @param userRepository Repositório para gerenciar os dados de usuários.
     * @param roleRepository Repositório para gerenciar as roles (permissões).
     * @param passwordEncoder Codificador de senhas para armazenamento seguro.
     */
    public UserController(UserRepository userRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Endpoint para criar um novo usuário.
     *
     * <p>
     * Este endpoint recebe os dados do novo usuário, verifica se o e-mail já
     * está cadastrado e, caso contrário, cria um novo usuário com a role básica
     * "USER".</p>
     *
     * @param dto Objeto {@link CreateUserDto} contendo os dados do novo
     * usuário.
     * @return {@link ResponseEntity} com status HTTP 200 (OK) em caso de
     * sucesso.
     * @throws ResponseStatusException Com status 422 (UNPROCESSABLE ENTITY)
     * caso o e-mail já esteja cadastrado.
     */
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

    /**
     * Endpoint para listar todos os usuários.
     *
     * <p>
     * Este endpoint retorna uma lista com todos os usuários cadastrados no
     * sistema. O acesso é restrito a usuários com a permissão
     * "SCOPE_ADMIN".</p>
     *
     * @return {@link ResponseEntity} contendo a lista de usuários.
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Endpoint alternativo para listar todos os usuários.
     *
     * <p>
     * Este endpoint retorna uma lista com todos os usuários cadastrados no
     * sistema. O acesso é restrito a usuários com a permissão "SCOPE_USER".</p>
     *
     * @return {@link ResponseEntity} contendo a lista de usuários.
     */
    @GetMapping("/users2")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<List<User>> listUsersTwo() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}

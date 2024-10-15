/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spring.OAuth2.config;

import com.spring.OAuth2.entities.Role;
import com.spring.OAuth2.entities.User;
import com.spring.OAuth2.repository.RoleRepository;
import com.spring.OAuth2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Dan
 */
@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository,
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdminConfig = roleRepository.findByName(Role.Values.ADMIN.name());
        if (roleAdminConfig == null) {
            roleAdminConfig = new Role();
            roleAdminConfig.setName(Role.Values.ADMIN.name());
            roleRepository.save(roleAdminConfig);
        } else {
            System.out.println("Role Admin ja existe");
        }

        // Verifica se a role USER já existe, caso contrário, cria a role USER
        var roleUserConfig = roleRepository.findByName(Role.Values.USER.name());
        if (roleUserConfig == null) {
            roleUserConfig = new Role();
            roleUserConfig.setName(Role.Values.USER.name());
            roleRepository.save(roleUserConfig);
        } else {
            System.out.println("Role User ja existe");
        }

        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
        var userAdmin = userRepository.findByUserEmail("admin@admin.com");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin ja existe");
                },
                () -> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setUserEmail("admin@admin.com");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );

        var roleUser = roleRepository.findByName(Role.Values.USER.name());
        var userUser = userRepository.findByUserEmail("user@user.com");
        userUser.ifPresentOrElse(
                user -> {
                    System.out.println("user já existe");
                },
                () -> {
                    var user = new User();
                    user.setUsername("user");
                    user.setUserEmail("user@user.com");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleUser)); // Define o role USER
                    userRepository.save(user);
                }
        );
    }
}
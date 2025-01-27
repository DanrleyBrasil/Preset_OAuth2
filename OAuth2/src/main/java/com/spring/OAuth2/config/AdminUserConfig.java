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
 * Classe de configuração responsável pela inicialização de usuários e roles
 * administrativas no sistema.
 *
 * <p>
 * Esta classe verifica se as roles "ADMIN" e "USER" já existem, e as cria caso
 * necessário. Além disso, verifica se o usuário "admin" e "user" existem no
 * sistema e os cria, com configurações padrão, caso não estejam
 * cadastrados.</p>
 *
 * @author danrleybrasil
 */
@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Construtor da classe AdminUserConfig.
     *
     * @param roleRepository Repositório responsável pelas operações com as
     * roles.
     * @param userRepository Repositório responsável pelas operações com os
     * usuários.
     * @param passwordEncoder Codificador de senhas usado para salvar senhas de
     * forma segura.
     */
    public AdminUserConfig(RoleRepository roleRepository,
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Método executado ao iniciar a aplicação.
     *
     * <p>
     * Este método realiza as seguintes operações:</p>
     * <ul>
     * <li>Verifica se a role "ADMIN" existe no banco de dados e a cria caso não
     * exista.</li>
     * <li>Verifica se a role "USER" existe no banco de dados e a cria caso não
     * exista.</li>
     * <li>Verifica se o usuário "admin" está cadastrado e o cria com
     * configurações padrão caso não esteja.</li>
     * <li>Verifica se o usuário "user" está cadastrado e o cria com
     * configurações padrão caso não esteja.</li>
     * </ul>
     *
     * @param args Argumentos passados para o método principal da aplicação.
     * @throws Exception Caso ocorra algum erro durante a execução.
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Verifica se a role ADMIN já existe
        var roleAdminConfig = roleRepository.findByName(Role.Values.ADMIN.name());
        if (roleAdminConfig == null) {
            roleAdminConfig = new Role();
            roleAdminConfig.setName(Role.Values.ADMIN.name());
            roleRepository.save(roleAdminConfig);
        } else {
            System.out.println("Role Admin já existe");
        }

        // Verifica se a role USER já existe
        var roleUserConfig = roleRepository.findByName(Role.Values.USER.name());
        if (roleUserConfig == null) {
            roleUserConfig = new Role();
            roleUserConfig.setName(Role.Values.USER.name());
            roleRepository.save(roleUserConfig);
        } else {
            System.out.println("Role User já existe");
        }

        // Verifica se o usuário admin já existe
        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
        var userAdmin = userRepository.findByUserEmail("admin@admin.com");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("Admin já existe");
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

        // Verifica se o usuário user já existe
        var roleUser = roleRepository.findByName(Role.Values.USER.name());
        var userUser = userRepository.findByUserEmail("user@user.com");
        userUser.ifPresentOrElse(
                user -> {
                    System.out.println("User já existe");
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

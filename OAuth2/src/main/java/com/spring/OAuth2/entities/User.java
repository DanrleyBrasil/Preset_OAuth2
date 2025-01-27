/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spring.OAuth2.entities;

import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.spring.OAuth2.controller.dto.LoginRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um usuário no sistema.
 * 
 * <p>Esta classe define as informações básicas de um usuário, incluindo
 * identificador único, e-mail, nome de usuário, senha e suas permissões (roles).</p>
 * 
 * <p>Os usuários possuem uma relação Many-to-Many com a entidade {@link Role}, 
 * permitindo que cada usuário tenha múltiplas roles associadas.</p>
 * 
 * <p>Além disso, fornece um método para validar se as credenciais de login 
 * fornecidas estão corretas.</p>
 * 
 * @author danrleybrasil
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_users")
public class User {

    /**
     * Identificador único do usuário.
     * 
     * <p>Gerado automaticamente utilizando o padrão UUID.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    /**
     * E-mail único do usuário.
     */
    @Column(unique = true)
    private String userEmail;

    /**
     * Nome de usuário único.
     */
    @Column(unique = true)
    private String username;

    /**
     * Senha do usuário.
     * 
     * <p>A senha é armazenada de forma segura utilizando um codificador 
     * de senhas (PasswordEncoder).</p>
     */
    private String password;

    /**
     * Conjunto de roles (permissões) associadas ao usuário.
     * 
     * <p>A relação é mapeada em uma tabela de junção chamada "tb_users_roles".</p>
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tb_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    /**
     * Verifica se as credenciais de login fornecidas estão corretas.
     * 
     * <p>Este método compara a senha fornecida no {@link LoginRequest} com a 
     * senha armazenada para o usuário, utilizando um codificador de senhas.</p>
     * 
     * @param loginRequest Objeto contendo as credenciais fornecidas pelo usuário.
     * @param passwordEncoder Instância de {@link PasswordEncoder} usada para validar a senha.
     * @return {@code true} se a senha estiver correta; {@code false} caso contrário.
     */
    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequest.password(), this.password);
    }
}

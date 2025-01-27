/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.spring.OAuth2.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.OAuth2.entities.User;

/**
 * Repositório responsável pelo gerenciamento das operações relacionadas à
 * entidade {@link User}.
 *
 * <p>
 * Extende a interface {@link JpaRepository}, fornecendo métodos padrão para
 * operações de CRUD (Create, Read, Update, Delete) na base de dados.</p>
 *
 * <p>
 * Inclui também um método personalizado para buscar um usuário pelo e-mail.</p>
 *
 * @author danrleybrasil
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Busca um usuário pelo e-mail.
     *
     * @param userEmail E-mail do usuário a ser buscado.
     * @return Um {@link Optional} contendo a instância de {@link User}
     * correspondente ao e-mail fornecido, ou vazio ({@code Optional.empty()})
     * se o usuário não for encontrado.
     */
    Optional<User> findByUserEmail(String userEmail);

}

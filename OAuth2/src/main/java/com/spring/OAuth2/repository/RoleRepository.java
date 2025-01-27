/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spring.OAuth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.OAuth2.entities.Role;

/**
 * Repositório responsável pelo gerenciamento das operações relacionadas à
 * entidade {@link Role}.
 *
 * <p>
 * Extende a interface {@link JpaRepository}, fornecendo métodos padrão para
 * operações de CRUD (Create, Read, Update, Delete) na base de dados.</p>
 *
 * <p>
 * Inclui também um método personalizado para buscar uma role pelo nome.</p>
 *
 * @author danrleybrasil
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca uma role pelo nome.
     *
     * @param name Nome da role a ser buscada.
     * @return Instância de {@link Role} correspondente ao nome fornecido, ou
     * {@code null} se não encontrada.
     */
    Role findByName(String name);
}

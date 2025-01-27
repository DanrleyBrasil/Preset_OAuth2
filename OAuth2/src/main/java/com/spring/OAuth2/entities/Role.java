/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spring.OAuth2.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma role (papel) no sistema.
 *
 * <p>
 * Uma role define as permissões e os acessos que um usuário pode ter na
 * aplicação. Cada role possui um identificador único (roleId) e um nome.</p>
 *
 * <p>
 * A classe também inclui uma enumeração {@link Values} que define os valores
 * padrão de roles no sistema, como ADMIN e USER.</p>
 *
 * @author danrleybrasil
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_roles")
public class Role {

    /**
     * Identificador único da role.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    /**
     * Nome da role.
     */
    private String name;

    /**
     * Enumeração que define os valores padrão das roles do sistema.
     *
     * <p>
     * Contém os seguintes valores:</p>
     * <ul>
     * <li><b>ADMIN</b>: Representa a role de administrador, com ID 1.</li>
     * <li><b>USER</b>: Representa a role de usuário comum, com ID 2.</li>
     * </ul>
     */
    public enum Values {
        ADMIN(1L),
        USER(2L);

        private final long roleId;

        /**
         * Construtor da enumeração Values.
         *
         * @param roleId Identificador único da role.
         */
        Values(long roleId) {
            this.roleId = roleId;
        }

        /**
         * Retorna o identificador único da role.
         *
         * @return Identificador da role.
         */
        public long getRoleId() {
            return this.roleId;
        }
    }

}

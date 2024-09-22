/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.spring.OAuth2.repository;

import com.spring.OAuth2.entities.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dan
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUserEmail(String userEmail);

}

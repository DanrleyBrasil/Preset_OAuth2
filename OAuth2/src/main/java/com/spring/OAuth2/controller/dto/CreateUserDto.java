/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.spring.OAuth2.controller.dto;

/**
 * DTO (Data Transfer Object) utilizado para transferir os dados necessários
 * para a criação de um novo usuário.
 * 
 * <p>Contém os seguintes campos:</p>
 * <ul>
 *   <li><b>userName</b>: Nome do usuário.</li>
 *   <li><b>userEmail</b>: E-mail do usuário.</li>
 *   <li><b>password</b>: Senha do usuário.</li>
 * </ul>
 * 
 * @param userName Nome do usuário.
 * @param userEmail E-mail do usuário.
 * @param password Senha do usuário.
 * 
 * <p>Este record é imutável e usado principalmente para representar dados 
 * de entrada na API.</p>
 * 
 * @author danrleybrasil
 */
public record CreateUserDto(String userName, String userEmail, String password) {

}

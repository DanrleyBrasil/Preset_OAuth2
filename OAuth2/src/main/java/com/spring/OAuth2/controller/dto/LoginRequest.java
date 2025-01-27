/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.spring.OAuth2.controller.dto;

/**
 * DTO (Data Transfer Object) utilizado para transferir os dados de autenticação 
 * necessários durante o login.
 * 
 * <p>Contém os seguintes campos:</p>
 * <ul>
 *   <li><b>userEmail</b>: E-mail do usuário para autenticação.</li>
 *   <li><b>password</b>: Senha do usuário.</li>
 * </ul>
 * 
 * @param userEmail E-mail do usuário.
 * @param password Senha do usuário.
 * 
 * <p>Este record é usado como entrada no endpoint de login da API.</p>
 * 
 * @author danrleybrasil
 */
public record LoginRequest(String userEmail, String password) {

}

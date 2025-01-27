/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.spring.OAuth2.controller.dto;

/**
 * DTO (Data Transfer Object) utilizado para retornar os dados de autenticação
 * após um login bem-sucedido.
 * 
 * <p>Contém os seguintes campos:</p>
 * <ul>
 *   <li><b>accessToken</b>: Token JWT gerado para autenticação do usuário.</li>
 *   <li><b>expiresIn</b>: Tempo, em segundos, até que o token expire.</li>
 * </ul>
 * 
 * @param accessToken Token de acesso JWT gerado após o login.
 * @param expiresIn Tempo de expiração do token, em segundos.
 * 
 * <p>Este record é usado como resposta no endpoint de login da API.</p>
 * 
 * @author danrleybrasil
 */
public record LoginResponse(String accessToken, Long expiresIn) {

}

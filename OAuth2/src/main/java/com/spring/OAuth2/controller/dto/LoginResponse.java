/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.spring.OAuth2.controller.dto;

/**
 *
 * @author Dan
 */
public record LoginResponse(String accessToken, Long expiresIn) {

}

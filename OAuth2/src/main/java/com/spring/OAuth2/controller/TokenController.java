/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spring.OAuth2.controller;

import com.spring.OAuth2.controller.dto.LoginRequest;
import com.spring.OAuth2.controller.dto.LoginResponse;
import com.spring.OAuth2.entities.Role;
import com.spring.OAuth2.repository.UserRepository;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador responsável pela geração de tokens JWT para autenticação de usuários.
 * 
 * <p>Este controlador contém o endpoint de login, que valida as credenciais do 
 * usuário e retorna um token JWT, contendo as informações de autenticação e autorização.</p>
 * 
 * @author danrleybrasil
 */
@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Construtor da classe TokenController.
     * 
     * @param jwtEncoder Codificador de tokens JWT.
     * @param userRepository Repositório para acesso aos dados dos usuários.
     * @param passwordEncoder Codificador de senhas.
     */
    public TokenController(JwtEncoder jwtEncoder,
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Endpoint de login para autenticação de usuários.
     * 
     * <p>Recebe as credenciais do usuário no corpo da requisição, verifica se o 
     * e-mail e a senha estão corretos e retorna um token JWT em caso de sucesso.</p>
     * 
     * <p>O token contém informações como o emissor, o ID do usuário, o horário de emissão, 
     * o tempo de expiração e as permissões (scopes) do usuário.</p>
     * 
     * @param loginRequest Objeto contendo as credenciais do usuário (e-mail e senha).
     * @return Um {@link ResponseEntity} contendo o token JWT e o tempo de expiração.
     * @throws BadCredentialsException Se o e-mail ou a senha forem inválidos.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var user = userRepository.findByUserEmail(loginRequest.userEmail());
        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("user or password is invalid");
        }

        var now = Instant.now();
        var expiresIn = 30000L;

        // Obtém as permissões (scopes) do usuário
        var scopes = user.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        // Cria as informações (claims) do token JWT
        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        // Codifica e gera o token JWT
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }

}

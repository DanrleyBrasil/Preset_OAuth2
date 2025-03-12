/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.spring.OAuth2.controller;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.OAuth2.controller.dto.LoginRequest;
import com.spring.OAuth2.entities.Role;
import com.spring.OAuth2.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador responsável pela geração de tokens JWT para autenticação de
 * usuários.
 *
 * <p>
 * Este controlador contém o endpoint de login, que valida as credenciais do
 * usuário e retorna um token JWT, contendo as informações de autenticação e
 * autorização.</p>
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
     * <p>
     * Recebe as credenciais do usuário no corpo da requisição, verifica se o
     * e-mail e a senha estão corretos e retorna um token JWT em caso de
     * sucesso.</p>
     *
     * <p>
     * O token contém informações como o emissor, o ID do usuário, o horário de
     * emissão, o tempo de expiração e as permissões (scopes) do usuário.</p>
     *
     * @param loginRequest Objeto contendo as credenciais do usuário (e-mail e
     * senha).
     * @return Um {@link ResponseEntity} contendo o token JWT e o tempo de
     * expiração.
     * @throws BadCredentialsException Se o e-mail ou a senha forem inválidos.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        var user = userRepository.findByUserEmail(loginRequest.userEmail());
        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("Usuário ou senha inválidos.");
        }

        var now = Instant.now();
        var expiresIn = 3600L; // 1 hora

        var scopes = user.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        // ✅ Criar um cookie seguro com HttpOnly
        Cookie jwtCookie = new Cookie("jwt", jwtValue);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // ⚠ Para localhost, precisa ser false. Em produção, use true.
        jwtCookie.setPath("/"); // Disponível para toda a aplicação
        jwtCookie.setMaxAge((int) expiresIn);

        response.addCookie(jwtCookie);

        return ResponseEntity.ok("Login bem-sucedido! Cookie configurado.");
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    cookie.setValue(""); // 🔥 Limpa o valor
                    cookie.setPath("/");
                    cookie.setMaxAge(0); // 🔥 Expira imediatamente
                    cookie.setHttpOnly(true);
                    cookie.setSecure(false); // ⚠️ Em produção, use `true` com HTTPS
                    response.addCookie(cookie);

                }
            }
        }

        return ResponseEntity.ok("Logout realizado com sucesso.");
    }

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            throw new IllegalStateException("Usuário não autenticado");
        }

        return Map.of(
                "userId", jwt.getSubject(),
                "roles", jwt.getClaim("scope")
        );

    }
}

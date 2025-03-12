package com.spring.OAuth2.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração de segurança da aplicação utilizando Spring Security e JWT via
 * Cookies.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtCookieFilter jwtCookieFilter;

    public SecurityConfig(JwtCookieFilter jwtCookieFilter) {
        this.jwtCookieFilter = jwtCookieFilter;
    }

    /**
     * Configura os caminhos que serão ignorados pelo filtro de segurança.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/swagger-ui/**", "/v3/api-docs/**");
    }

    /**
     * Configura a segurança da API, incluindo CORS, autenticação JWT via
     * Cookies e permissões.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
            var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
            corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173")); // Permitir front-end local
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
            corsConfiguration.setAllowCredentials(true); // ✅ Permitir envio de Cookies
            return corsConfiguration;
        }))
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/login").permitAll() // Permitir login sem autenticação
                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // ❗ Desativamos CSRF pois usamos JWT Stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT não usa sessão
                .addFilterBefore(jwtCookieFilter, UsernamePasswordAuthenticationFilter.class);// ✅ Adiciona filtro JWT antes da autenticação

        return http.build();
    }

    /**
     * Configura o codificador de senhas utilizando BCrypt.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

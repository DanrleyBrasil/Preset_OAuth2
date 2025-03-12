package com.spring.OAuth2.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtCookieFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;

    public JwtCookieFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Optional<Cookie> jwtCookie = Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .findFirst());

        if (jwtCookie.isPresent()) {
            try {
                String token = jwtCookie.get().getValue();
                Jwt jwt = jwtDecoder.decode(token);

                // ✅ Pegando as roles do token
                Collection<GrantedAuthority> authorities = Arrays.stream(jwt.getClaimAsString("scope").split(" "))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // ✅ Criando um objeto de autenticação e adicionando ao contexto de segurança
                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(jwt, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("⚠️ Erro ao validar token JWT: " + e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}

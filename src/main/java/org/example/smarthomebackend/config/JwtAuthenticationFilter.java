package org.example.smarthomebackend.config;

import lombok.NonNull;
import org.example.smarthomebackend.service.jwt.JwtTokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtTokenService jwtTokenService;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
        super();
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(authHeader -> authHeader.substring(7))
                .flatMap(token -> {
                    if (jwtTokenService.validateToken(token)) {
                        String username = jwtTokenService.extractUsername(token);
                        return Mono.just(new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList()));
                    } else {
                        return Mono.empty();
                    }
                })
                .doOnNext(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication))
                .then(chain.filter(exchange));
    }
}

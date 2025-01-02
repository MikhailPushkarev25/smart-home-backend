package org.example.smarthomebackend.controller;

import org.example.smarthomebackend.service.jwt.AuthenticationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final AuthenticationService authenticationService;

    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public Mono<String> getLoginToken(@RequestParam String email, @RequestParam String password) {
        return authenticationService.authenticate(email, password);
    }
}

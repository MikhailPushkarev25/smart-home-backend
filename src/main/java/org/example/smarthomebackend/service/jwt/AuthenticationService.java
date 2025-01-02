package org.example.smarthomebackend.service.jwt;

import org.example.smarthomebackend.entity.User;
import org.example.smarthomebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository, JwtTokenService jwtTokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    // Аутентификация пользователя
    public Mono<String> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(createNewUser(email, password))
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPasswordHash())) {
                        // Генерация токена, если пароль совпадает
                        return Mono.just(jwtTokenService.generateToken(user.getUsername()));
                    } else {
                        return Mono.error(new BadCredentialsException("Invalid credentials"));
                    }
                });
    }


    private Mono<User> createNewUser(String email, String password) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPasswordHash(passwordEncoder.encode(password));
        newUser.setUsername(email.split("@")[0]);
        newUser.setCreatedAt(LocalDateTime.now());
        return userRepository.save(newUser);
    }
}

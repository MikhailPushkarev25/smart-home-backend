package org.example.smarthomebackend.service.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private static final Duration TOKEN_VALIDITY = Duration.ofHours(1);

    public JwtTokenService() throws Exception {
        KeyPair keyPair = generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    // Метод для генерации ключевой пары
    private KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);  // Размер ключа в битах
        return keyPairGenerator.generateKeyPair();
    }

    // Генерация JWT токена
    public String generateToken(String username) {

        // Claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        // Генерация и подпись токена
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date())
                .signWith(privateKey)  // Подписываем токен с использованием приватного ключа
                .compact();
    }

    // Валидация токена
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(publicKey)  // Проверка подписи с использованием публичного ключа
                    .build()
                    .parseUnsecuredClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Извлечение имени пользователя из токена
    public String extractUsername(String token) {
        return (String) Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseUnsecuredClaims(token)
                .getPayload()
                .get("username");
    }
}

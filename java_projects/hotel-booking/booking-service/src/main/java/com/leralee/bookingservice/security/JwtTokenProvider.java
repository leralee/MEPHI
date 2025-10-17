package com.leralee.bookingservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Component
public class JwtTokenProvider {

    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String secretKey;

    private final long validityMs = 3600000; // 1 час

    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }
}

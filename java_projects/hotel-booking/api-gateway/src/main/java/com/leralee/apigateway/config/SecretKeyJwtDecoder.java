package com.leralee.apigateway.config;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Simple JWT decoder using symmetric secret for gateway resource server.
 */
public class SecretKeyJwtDecoder implements JwtDecoder {

    private final JwtDecoder delegate;

    public SecretKeyJwtDecoder(String secret) {
        SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        this.delegate = NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        return delegate.decode(token);
    }
}

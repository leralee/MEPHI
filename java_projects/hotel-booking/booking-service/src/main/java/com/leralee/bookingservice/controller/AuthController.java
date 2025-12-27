package com.leralee.bookingservice.controller;

import com.leralee.bookingservice.entity.User;
import com.leralee.bookingservice.security.JwtTokenProvider;
import com.leralee.bookingservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author valeriali
 * @project hotel-booking
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User saved = userService.create(user);
        String token = jwtTokenProvider.generateToken(saved.getUsername(), saved.getRole());
        return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody User user) {
        User existing = userService.authenticate(user.getUsername(), user.getPassword());
        String token = jwtTokenProvider.generateToken(existing.getUsername(), existing.getRole());
        return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
    }
}

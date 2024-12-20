package org.example.service;

import org.example.repository.InMemoryUserRepository;

import java.util.UUID;

/**
 * @author valeriali
 * @project short_links_service
 */
public class UserService {
    private final InMemoryUserRepository userRepository;

    public UserService(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUserId() {
        UUID newUser = UUID.randomUUID();
        userRepository.addUser(newUser);
        return newUser;
    }

    public boolean userExists(UUID userId) {
        return userRepository.userExists(userId);
    }
}

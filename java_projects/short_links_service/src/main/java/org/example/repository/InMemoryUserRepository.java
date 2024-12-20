package org.example.repository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author valeriali
 * @project short_links_service
 */
public class InMemoryUserRepository {
    private final Set<UUID> users = new HashSet<>();

    public void addUser(UUID userId) {
        users.add(userId);
    }

    public boolean userExists(UUID userId) {
        return users.contains(userId);
    }
}

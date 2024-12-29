package ru.mephi.finance.service;

import ru.mephi.finance.model.User;

import java.util.Map;
import java.util.Optional;

/**
 * @author valeriali
 * @project FinanceApp
 */
public class AuthService {
    private final Map<String, User> users;

    public AuthService(Map<String, User> users) {
        this.users = users;
    }

    public boolean register(String login, String password) {
        if (users.containsKey(login)) {
            return false; // Пользователь уже существует
        }
        users.put(login, new User(login, password));
        return true;
    }

    public Optional<User> login(String login, String password) {
        return Optional.ofNullable(users.get(login))
                .filter(user -> user.getPassword().equals(password));
    }
}

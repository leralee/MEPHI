package ru.mephi.finance.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.mephi.finance.model.User;
import ru.mephi.finance.model.Wallet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author valeriali
 * @project FinanceApp
 */
public class DataStorage {
    private static final String USER_FILE = "users.json";
    private static final String WALLET_FILE = "wallets.json";

    private final ObjectMapper objectMapper;

    public DataStorage() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // Сохранение данных в файл
    public void saveWallets(Map<String, Wallet> wallets) {
        try {
            objectMapper.writeValue(new File(WALLET_FILE), wallets);
            System.out.println("Данные успешно сохранены.");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    // Загрузка данных из файла
    public Map<String, Wallet> loadWallets() {
        File file = new File(WALLET_FILE);
        if (!file.exists()) {
            System.out.println("Файл wallets.json не найден. Создаётся пустая коллекция.");
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<Map<String, Wallet>>() {});
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке данных кошельков: " + e.getMessage());
            return new HashMap<>();
        }

    }

    public Map<String, User> loadUsers() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<Map<String, User>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void saveUsers(Map<String, User> users) {
        try {
            objectMapper.writeValue(new File(USER_FILE), users);
            System.out.println("Пользователи успешно сохранены.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

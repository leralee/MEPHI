package org.example;


import org.example.model.ShortLink;
import org.example.repository.InMemoryLinkRepository;
import org.example.repository.InMemoryUserRepository;
import org.example.service.ConfigurationService;
import org.example.service.LinkShortenerService;
import org.example.service.UserService;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException {
        InMemoryLinkRepository linkRepository = new InMemoryLinkRepository();
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        ConfigurationService configService = new ConfigurationService();
        LinkShortenerService linkService = new LinkShortenerService(linkRepository, configService);
        UserService userService = new UserService(userRepository);

        Scanner scanner = new Scanner(System.in);
        UUID userId = null;

        System.out.println("Добро пожаловать в сервис сокращения ссылок!");
        System.out.println("Введите команду (help для списка команд)");

        boolean running = true;
        while (running) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String command = parts[0].toLowerCase();

            switch (command) {
                case "help":
                    System.out.println("Команды:");
                    System.out.println("register - зарегистрировать нового пользователя");
                    System.out.println("login <uuid> - войти под существующим пользователем");
                    System.out.println("shorten <url> <max_clicks> <ttl_hours> - сократить ссылку");
                    System.out.println("open <shortUrl> - открыть короткую ссылку");
                    System.out.println("edit_limit <shortUrl> <new_limit> - изменить лимит переходов");
                    System.out.println("delete <shortUrl> - удалить ссылку");
                    System.out.println("cleanup - удалить истекшие ссылки");
                    System.out.println("exit - выйти");
                    break;
                case "register":
                    UUID newUserId = userService.createUserId();
                    System.out.println("Новый пользователь зарегистрирован с UUID: " + newUserId);
                    // Cразу сделали логин, чтобы пользователь не делал лишний шаг
                    userId = newUserId;
                    System.out.println("Вы вошли как пользователь: " + userId);
                    break;
                case "login":
                    if (parts.length < 2) {
                        System.out.println("Использование: login <uuid>");
                        break;
                    }
                    try {
                        UUID loginUserId = UUID.fromString(parts[1]);
                        if (userService.userExists(loginUserId)) {
                            userId = loginUserId;
                            System.out.println("Вы вошли как пользователь: " + userId);
                        } else {
                            System.out.println("Пользователь с таким UUID не зарегистрирован.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неверный формат UUID.");
                    }
                    break;
                case "shorten":
                    if (userId == null) {
                        System.out.println("Сначала зарегистрируйтесь (команда register).");
                        break;
                    }
                    if (parts.length < 4) {
                        System.out.println("Использование: shorten <url> <max_clicks> <ttl_hours>");
                        break;
                    }
                    try {
                        String url = parts[1];
                        int maxClicks = Integer.parseInt(parts[2]);
                        int ttlHours = Integer.parseInt(parts[3]);
                        ShortLink link = linkService.createShortLink(userId, url, maxClicks, ttlHours);
                        System.out.println("Короткая ссылка: " + link.getShortUrl());
                    } catch (NumberFormatException e) {
                        System.out.println("Неверный формат числа для max_clicks или ttl_hours");
                    }
                    break;
                case "open":
                    if (parts.length < 2) {
                        System.out.println("Использование: open <shortUrl>");
                        break;
                    }
                    linkService.openLink(parts[1]);
                    break;
                case "edit_limit":
                    if (userId == null) {
                        System.out.println("Сначала зарегистрируйтесь.");
                        break;
                    }
                    if (parts.length < 3) {
                        System.out.println("Использование: edit_limit <shortUrl> <new_limit>");
                        break;
                    }
                    try {
                        String shortUrl = parts[1];
                        int newLimit = Integer.parseInt(parts[2]);
                        linkService.editLimit(userId, shortUrl, newLimit);
                    } catch (NumberFormatException e) {
                        System.out.println("Неверный формат для new_limit");
                    }
                    break;
                case "delete":
                    if (userId == null) {
                        System.out.println("Сначала зарегистрируйтесь.");
                        break;
                    }
                    if (parts.length < 2) {
                        System.out.println("Использование: delete <shortUrl>");
                        break;
                    }
                    linkService.deleteLink(userId, parts[1]);
                    break;
                case "cleanup":
                    linkService.removeExpiredLinks();
                    System.out.println("Истекшие ссылки удалены.");
                    break;
                case "exit":
                    running = false;
                    break;
                default:
                    System.out.println("Неизвестная команда. help для списка команд.");
            }
        }

        scanner.close();
    }
}
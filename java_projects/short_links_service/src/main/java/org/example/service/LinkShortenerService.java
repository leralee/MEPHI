package org.example.service;

import org.example.model.ShortLink;
import org.example.repository.InMemoryLinkRepository;
import org.example.util.UrlShortener;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;


/**
 * @author valeriali
 * @project short_links_service
 */
public class LinkShortenerService {
    private final InMemoryLinkRepository linkRepository;
    private final ConfigurationService configService;

    public LinkShortenerService(InMemoryLinkRepository repository, ConfigurationService configService) {
        this.linkRepository = repository;
        this.configService = configService;
    }

    public ShortLink createShortLink(UUID userId, String originalUrl, int userMaxClicks, int userTtlHours) {
        // Рассчитываем фактическое время жизни и лимит кликов
        int actualTtlHours = Math.min(userTtlHours, configService.getMaxTtlHours());
        int actualMaxClicks = Math.max(userMaxClicks, configService.getMinMaxClicks());

        String code = UrlShortener.generateShortCode();
        // Допустим, базовый домен:
        String shortUrl = "http://short.link/" + code;

        long ttlMillis = Duration.ofHours(actualTtlHours).toMillis();
        ShortLink link = new ShortLink(shortUrl, originalUrl, userId, actualMaxClicks, ttlMillis);
        linkRepository.save(link);
        return link;
    }

    public void openLink(String shortUrl) {
        ShortLink link = linkRepository.findByShortUrl(shortUrl);
        if (link == null) {
            System.out.println("Ссылка не найдена.");
            return;
        }

        // Проверяем активность
        if (!link.isActive() || link.isExpired() || link.isLimitReached()) {
            System.out.println("Ссылка недоступна. Истёк срок или достигнут лимит переходов.");
            link.deactivate();
            // Сразу удаляем такую ссылку, чтобы не копился мусор
            linkRepository.deleteByShortUrl(shortUrl);
            System.out.println("Ссылка удалена, так как она недоступна.");
            return;
        }

        link.incrementClicks();
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(link.getOriginalUrl()));
            } else {
                System.out.println("Откройте ссылку вручную: " + link.getOriginalUrl());
            }
            System.out.println("Переход по ссылке: " + link.getOriginalUrl());
        } catch (Exception e) {
            System.out.println("Не удалось открыть ссылку в браузере: " + e.getMessage());
        }

        // Проверяем, не достигнут ли лимит после клика
        if (link.isLimitReached()) {
            link.deactivate();
            System.out.println("Лимит переходов достигнут. Ссылка деактивирована.");
            linkRepository.deleteByShortUrl(shortUrl);
            System.out.println("Ссылка удалена.");
        }
    }

    public void removeExpiredLinks() {
        linkRepository.findAll().values().removeIf(ShortLink::isExpired);
        System.out.println("Все истекшие ссылки удалены.");
    }

    public boolean editLimit(UUID userId, String shortUrl, int newLimit) {
        ShortLink link = linkRepository.findByShortUrl(shortUrl);
        if (link == null) {
            System.out.println("Ссылка не найдена.");
            return false;
        }
        if (!link.getUserId().equals(userId)) {
            System.out.println("Вы не являетесь владельцем этой ссылки.");
            return false;
        }

        // Новый лимит не может быть меньше minMaxClicks из конфигурации
        int actualNewLimit = Math.max(newLimit, configService.getMinMaxClicks());
        // Так как ShortLink у нас неизменяем по лимиту, можно сделать сеттер или пересоздать объект
        link.setMaxClicks(actualNewLimit);
        System.out.println("Лимит переходов изменён на " + actualNewLimit);
        return true;
    }

    public boolean deleteLink(UUID userId, String shortUrl) {
        ShortLink link = linkRepository.findByShortUrl(shortUrl);
        if (link == null) {
            System.out.println("Ссылка не найдена.");
            return false;
        }
        if (!link.getUserId().equals(userId)) {
            System.out.println("Вы не являетесь владельцем этой ссылки.");
            return false;
        }

        linkRepository.deleteByShortUrl(shortUrl);
        System.out.println("Ссылка удалена.");
        return true;
    }
}

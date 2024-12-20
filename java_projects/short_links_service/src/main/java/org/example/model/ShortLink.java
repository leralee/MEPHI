package org.example.model;

import java.time.Instant;
import java.util.UUID;


/**
 * @author valeriali
 * @project short_links_service
 */
public class ShortLink {
    private final String shortUrl;
    private final String originalUrl;
    private final UUID userId;
    private int maxClicks;
    private int currentClicks;
    private final Instant creationTime;
    private final long ttlMillis; // время жизни в миллисекундах
    private boolean active;

    public ShortLink(String shortUrl, String originalUrl, UUID userId, int maxClicks, long ttlMillis) {
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.userId = userId;
        this.maxClicks = maxClicks;
        this.ttlMillis = ttlMillis;
        this.currentClicks = 0;
        this.creationTime = Instant.now();
        this.active = true;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public UUID getUserId() {
        return userId;
    }

    public int getMaxClicks() {
        return maxClicks;
    }

    public void setMaxClicks(int maxClicks) {
        this.maxClicks = maxClicks;
    }

    public int getCurrentClicks() {
        return currentClicks;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public long getTtlMillis() {
        return ttlMillis;
    }

    public boolean isActive() {
        return active;
    }

    public void incrementClicks() {
        currentClicks++;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isExpired() {
        return Instant.now().toEpochMilli() > (creationTime.toEpochMilli() + ttlMillis);
    }

    public boolean isLimitReached() {
        return currentClicks >= maxClicks;
    }
}

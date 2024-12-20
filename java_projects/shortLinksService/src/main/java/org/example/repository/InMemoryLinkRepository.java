package org.example.repository;


import org.example.model.ShortLink;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author valeriali
 * @project short_links_service
 */
public class InMemoryLinkRepository {
    private final Map<String, ShortLink> links = new ConcurrentHashMap<>();

    public ShortLink save(ShortLink link) {
        links.put(link.getShortUrl(), link);
        return link;
    }

    public ShortLink findByShortUrl(String shortUrl) {
        return links.get(shortUrl);
    }

    public void deleteByShortUrl(String shortUrl) {
        links.remove(shortUrl);
    }

    public Map<String, ShortLink> findAll() {
        return links;
    }
}

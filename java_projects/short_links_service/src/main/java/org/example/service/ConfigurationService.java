package org.example.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author valeriali
 * @project short_links_service
 */
public class ConfigurationService {
    private final Properties props = new Properties();
    private final int maxTtlHours;
    private final int minMaxClicks;

    public ConfigurationService() {
        try (InputStream is = ConfigurationService.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.properties not found");
            }
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }

        this.maxTtlHours = Integer.parseInt(props.getProperty("max_ttl_hours", "24"));
        this.minMaxClicks = Integer.parseInt(props.getProperty("min_max_clicks", "5"));
    }

    public int getMaxTtlHours() {
        return maxTtlHours;
    }

    public int getMinMaxClicks() {
        return minMaxClicks;
    }
}

package org.example.util;
import java.util.Random;

/**
 * @author valeriali
 * @project short_links_service
 */
public class UrlShortener {
    // Простой генератор коротких ссылок — в реальности стоит использовать более надёжный механизм
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 6;
    private static final Random RANDOM = new Random();

    public static String generateShortCode() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}


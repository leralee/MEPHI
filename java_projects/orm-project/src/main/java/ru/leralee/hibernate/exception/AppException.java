package ru.leralee.hibernate.exception;

import lombok.Getter;

/**
 * @author valeriali
 * @project orm-project
 */
@Getter
public class AppException extends RuntimeException {

    private final int httpCode;

    public AppException(String message, int httpCode) {
        super(message);
        this.httpCode = httpCode;
    }
}

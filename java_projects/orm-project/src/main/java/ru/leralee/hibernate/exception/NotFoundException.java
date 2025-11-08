package ru.leralee.hibernate.exception;

/**
 * @author valeriali
 * @project orm-project
 */
public class NotFoundException extends AppException {

    public NotFoundException(String message) {
        super(message, 404);
    }
}

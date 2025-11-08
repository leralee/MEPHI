package ru.leralee.hibernate.web;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author valeriali
 * @project orm-project
 */
@Value
@Builder
public class ApiErrorResponse {

    LocalDateTime timestamp;
    int status;
    String error;
    String message;
    List<String> details;
    String path;
}

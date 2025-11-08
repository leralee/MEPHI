package ru.leralee.hibernate.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class CategoryResponse {

    private UUID id;
    private String name;
    private String slug;
    private LocalDateTime createdAt;
}

package ru.leralee.hibernate.dto.nested;

import lombok.Data;

import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class CategoryInfo {

    private UUID id;
    private String name;
    private String slug;
}

package ru.leralee.hibernate.dto.nested;

import lombok.Data;

import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class CourseInfo {

    private UUID id;
    private String code;
    private String title;
}

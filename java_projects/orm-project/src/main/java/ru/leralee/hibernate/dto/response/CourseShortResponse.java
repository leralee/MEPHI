package ru.leralee.hibernate.dto.response;

import lombok.Data;

import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class CourseShortResponse {

    private UUID id;
    private String code;
    private String title;
    private Integer credits;
}

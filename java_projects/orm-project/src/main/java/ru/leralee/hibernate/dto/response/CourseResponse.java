package ru.leralee.hibernate.dto.response;

import lombok.Data;
import ru.leralee.hibernate.dto.nested.AuthorInfo;
import ru.leralee.hibernate.dto.nested.CategoryInfo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class CourseResponse {

    private UUID id;
    private String code;
    private String title;
    private String description;
    private Integer credits;
    private Set<String> keywords = new HashSet<>();
    private AuthorInfo author;
    private CategoryInfo category;
    private LocalDateTime createdAt;
}

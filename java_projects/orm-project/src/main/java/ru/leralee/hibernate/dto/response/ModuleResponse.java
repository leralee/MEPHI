package ru.leralee.hibernate.dto.response;

import lombok.Data;
import ru.leralee.hibernate.dto.nested.CourseInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class ModuleResponse {

    private UUID id;
    private String title;
    private String description;
    private Integer orderIndex;
    private CourseInfo course;
    private List<LessonResponse> lessons = new ArrayList<>();
    private LocalDateTime createdAt;
}

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
public class LessonResponse {

    private UUID id;
    private String title;
    private String syllabus;
    private List<String> resources = new ArrayList<>();
    private CourseInfo course;
    private UUID moduleId;
    private LocalDateTime createdAt;
}

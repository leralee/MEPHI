package ru.leralee.hibernate.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class AssignmentResponse {

    private UUID id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Integer maxPoints;
    private UUID lessonId;
    private UUID moduleId;
    private UUID courseId;
    private LocalDateTime createdAt;
}

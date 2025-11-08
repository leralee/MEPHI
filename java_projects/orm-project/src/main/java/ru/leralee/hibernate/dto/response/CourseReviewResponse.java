package ru.leralee.hibernate.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class CourseReviewResponse {

    private UUID id;
    private UUID courseId;
    private UUID studentId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}

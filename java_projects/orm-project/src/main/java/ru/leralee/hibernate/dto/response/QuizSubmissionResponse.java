package ru.leralee.hibernate.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class QuizSubmissionResponse {

    private UUID id;
    private UUID quizId;
    private UUID studentId;
    private Double score;
    private Boolean passed;
    private LocalDateTime takenAt;
    private LocalDateTime createdAt;
}

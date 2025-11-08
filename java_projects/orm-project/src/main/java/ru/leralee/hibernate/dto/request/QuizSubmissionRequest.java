package ru.leralee.hibernate.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class QuizSubmissionRequest {

    @NotNull
    private UUID quizId;

    @NotNull
    private UUID studentId;

    @NotNull
    private Double score;

    private Boolean passed;
}

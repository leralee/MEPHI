package ru.leralee.hibernate.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class QuizResponse {

    private UUID id;
    private String title;
    private Integer timeLimitMinutes;
    private UUID moduleId;
    private List<QuestionResponse> questions = new ArrayList<>();
    private LocalDateTime createdAt;
}

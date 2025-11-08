package ru.leralee.hibernate.dto.response;

import lombok.Data;
import ru.leralee.hibernate.enums.QuestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class QuestionResponse {

    private UUID id;
    private String text;
    private QuestionType type;
    private Integer orderIndex;
    private UUID quizId;
    private List<AnswerOptionResponse> options = new ArrayList<>();
}

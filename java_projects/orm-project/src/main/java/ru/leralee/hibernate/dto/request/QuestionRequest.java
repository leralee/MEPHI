package ru.leralee.hibernate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.leralee.hibernate.enums.QuestionType;

import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class QuestionRequest {

    @NotNull
    private UUID quizId;

    @NotBlank
    @Size(max = 2000)
    private String text;

    @NotNull
    private QuestionType type;

    private Integer orderIndex;
}

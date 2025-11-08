package ru.leralee.hibernate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class AnswerOptionRequest {

    @NotNull
    private UUID questionId;

    @NotBlank
    @Size(max = 1000)
    private String text;

    private boolean correct;
}

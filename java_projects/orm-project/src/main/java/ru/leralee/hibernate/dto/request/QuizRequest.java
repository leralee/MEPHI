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
public class QuizRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    private Integer timeLimitMinutes;

    @NotNull
    private UUID moduleId;
}

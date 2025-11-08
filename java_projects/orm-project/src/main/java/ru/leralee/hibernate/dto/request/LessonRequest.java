package ru.leralee.hibernate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class LessonRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 5000)
    private String syllabus;

    private List<@Size(max = 500) String> resources = new ArrayList<>();

    @NotNull
    private UUID courseId;

    @NotNull
    private UUID moduleId;

    private UUID primaryResourceId;
}

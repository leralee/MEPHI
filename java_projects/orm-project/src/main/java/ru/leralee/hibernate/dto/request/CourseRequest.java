package ru.leralee.hibernate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class CourseRequest {

    @NotBlank
    @Size(max = 20)
    private String code;

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 2000)
    private String description;

    private Integer credits;

    private Set<@Size(min = 2, max = 50) String> keywords = new HashSet<>();

    @NotNull
    private UUID authorId;

    private UUID categoryId;

    private UUID featuredResourceId;
}

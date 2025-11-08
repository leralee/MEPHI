package ru.leralee.hibernate.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.leralee.hibernate.enums.SubmissionStatus;

import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class SubmissionRequest {

    @NotNull
    private UUID assignmentId;

    @NotNull
    private UUID studentId;

    @Size(max = 500)
    private String contentUrl;

    private String content;

    private SubmissionStatus status;

    @NotNull
    private Integer attemptNo;

    private Double score;

    @Size(max = 2000)
    private String feedback;
}

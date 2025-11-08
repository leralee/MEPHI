package ru.leralee.hibernate.dto.response;

import lombok.Data;
import ru.leralee.hibernate.enums.SubmissionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class SubmissionResponse {

    private UUID id;
    private UUID assignmentId;
    private UUID studentId;
    private SubmissionStatus status;
    private Integer attemptNo;
    private String contentUrl;
    private String content;
    private Double score;
    private String feedback;
    private LocalDateTime submittedAt;
    private LocalDateTime createdAt;
}

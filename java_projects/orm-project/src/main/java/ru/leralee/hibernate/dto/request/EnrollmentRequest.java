package ru.leralee.hibernate.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.leralee.hibernate.enums.EnrollmentRole;
import ru.leralee.hibernate.enums.EnrollmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class EnrollmentRequest {

    @NotNull
    private EnrollmentRole role;

    private LocalDateTime enrolledAt;

    @NotNull
    private EnrollmentStatus status;

    private Double finalGrade;

    @NotNull
    private UUID studentId;

    @NotNull
    private UUID courseId;
}

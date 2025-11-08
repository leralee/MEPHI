package ru.leralee.hibernate.dto.response;

import lombok.Data;
import ru.leralee.hibernate.dto.nested.CourseInfo;
import ru.leralee.hibernate.dto.nested.StudentInfo;
import ru.leralee.hibernate.enums.EnrollmentRole;
import ru.leralee.hibernate.enums.EnrollmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class EnrollmentResponse {

    private UUID id;
    private EnrollmentRole role;
    private LocalDateTime enrolledAt;
    private EnrollmentStatus status;
    private Double finalGrade;
    private StudentInfo student;
    private CourseInfo course;
    private LocalDateTime createdAt;
}

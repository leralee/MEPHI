package ru.leralee.hibernate.dto.response;

import lombok.Data;
import ru.leralee.hibernate.enums.StudentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class StudentResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String externalRef;
    private LocalDate birthDate;
    private String email;
    private String studentNo;
    private StudentStatus status;
    private Set<String> emails = new HashSet<>();
    private Set<String> phones = new HashSet<>();
    private LocalDateTime createdAt;
}

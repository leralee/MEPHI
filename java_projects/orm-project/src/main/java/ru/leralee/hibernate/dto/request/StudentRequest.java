package ru.leralee.hibernate.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.leralee.hibernate.enums.StudentStatus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class StudentRequest {

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @Size(max = 100)
    private String externalRef;

    private LocalDate birthDate;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_-]{3,20}$", message = "studentNo must be alphanumeric (3-20)")
    private String studentNo;

    @NotNull
    private StudentStatus status;

    private Set<@Email String> emails = new HashSet<>();

    private Set<@Size(min = 3, max = 20) String> phones = new HashSet<>();
}

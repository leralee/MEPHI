package ru.leralee.hibernate.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class TeacherRequest {

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

    @Size(max = 150)
    private String academicTitle;
}

package ru.leralee.hibernate.dto.nested;

import lombok.Data;

import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class AuthorInfo {

    private UUID id;
    private String firstName;
    private String lastName;
    private String academicTitle;
}

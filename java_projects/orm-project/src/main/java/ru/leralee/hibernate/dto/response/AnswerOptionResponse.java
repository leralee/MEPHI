package ru.leralee.hibernate.dto.response;

import lombok.Data;

import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Data
public class AnswerOptionResponse {

    private UUID id;
    private String text;
    private boolean correct;
}

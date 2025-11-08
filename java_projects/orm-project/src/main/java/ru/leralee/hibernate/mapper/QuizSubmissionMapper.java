package ru.leralee.hibernate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.leralee.hibernate.dto.request.QuizSubmissionRequest;
import ru.leralee.hibernate.dto.response.QuizSubmissionResponse;
import ru.leralee.hibernate.entity.QuizSubmission;

import java.util.List;

/**
 * @author valeriali
 * @project orm-project
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuizSubmissionMapper {

    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "student", ignore = true)
    QuizSubmission toEntity(QuizSubmissionRequest request);

    @Mapping(target = "quizId", source = "quiz.id")
    @Mapping(target = "studentId", source = "student.id")
    QuizSubmissionResponse toResponse(QuizSubmission submission);

    List<QuizSubmissionResponse> toResponseList(List<QuizSubmission> submissions);

    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(QuizSubmissionRequest request, @MappingTarget QuizSubmission submission);
}

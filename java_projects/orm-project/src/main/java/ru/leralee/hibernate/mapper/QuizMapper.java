package ru.leralee.hibernate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.leralee.hibernate.dto.request.QuizRequest;
import ru.leralee.hibernate.dto.response.QuizResponse;
import ru.leralee.hibernate.entity.Quiz;

import java.util.List;

/**
 * @author valeriali
 * @project orm-project
 */
@Mapper(componentModel = "spring", uses = {QuestionMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuizMapper {

    @Mapping(target = "module", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "submissions", ignore = true)
    Quiz toEntity(QuizRequest request);

    @Mapping(target = "moduleId", source = "module.id")
    QuizResponse toResponse(Quiz quiz);

    List<QuizResponse> toResponseList(List<Quiz> quizzes);

    @Mapping(target = "module", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "submissions", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(QuizRequest request, @MappingTarget Quiz quiz);
}

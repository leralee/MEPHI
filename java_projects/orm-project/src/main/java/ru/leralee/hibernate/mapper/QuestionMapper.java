package ru.leralee.hibernate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.leralee.hibernate.dto.request.QuestionRequest;
import ru.leralee.hibernate.dto.response.QuestionResponse;
import ru.leralee.hibernate.entity.Question;

import java.util.List;

/**
 * @author valeriali
 * @project orm-project
 */
@Mapper(componentModel = "spring", uses = {AnswerOptionMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionMapper {

    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "options", ignore = true)
    Question toEntity(QuestionRequest request);

    @Mapping(target = "quizId", source = "quiz.id")
    QuestionResponse toResponse(Question question);

    List<QuestionResponse> toResponseList(List<Question> questions);

    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "options", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(QuestionRequest request, @MappingTarget Question question);
}

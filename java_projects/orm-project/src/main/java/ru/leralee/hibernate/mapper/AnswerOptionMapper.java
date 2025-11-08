package ru.leralee.hibernate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.leralee.hibernate.dto.request.AnswerOptionRequest;
import ru.leralee.hibernate.dto.response.AnswerOptionResponse;
import ru.leralee.hibernate.entity.AnswerOption;

import java.util.List;

/**
 * @author valeriali
 * @project orm-project
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnswerOptionMapper {

    @Mapping(target = "question", ignore = true)
    AnswerOption toEntity(AnswerOptionRequest request);

    AnswerOptionResponse toResponse(AnswerOption option);

    List<AnswerOptionResponse> toResponseList(List<AnswerOption> options);

    @Mapping(target = "question", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(AnswerOptionRequest request, @MappingTarget AnswerOption option);
}

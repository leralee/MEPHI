package ru.leralee.hibernate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.leralee.hibernate.dto.request.SubmissionRequest;
import ru.leralee.hibernate.dto.response.SubmissionResponse;
import ru.leralee.hibernate.entity.Submission;

import java.util.List;

/**
 * @author valeriali
 * @project orm-project
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubmissionMapper {

    @Mapping(target = "assignment", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "grade", ignore = true)
    Submission toEntity(SubmissionRequest request);

    @Mapping(target = "assignmentId", source = "assignment.id")
    @Mapping(target = "studentId", source = "student.id")
    SubmissionResponse toResponse(Submission submission);

    List<SubmissionResponse> toResponseList(List<Submission> submissions);

    @Mapping(target = "assignment", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "grade", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(SubmissionRequest request, @MappingTarget Submission submission);
}

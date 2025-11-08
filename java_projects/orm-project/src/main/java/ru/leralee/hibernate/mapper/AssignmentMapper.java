package ru.leralee.hibernate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.leralee.hibernate.dto.request.AssignmentRequest;
import ru.leralee.hibernate.dto.response.AssignmentResponse;
import ru.leralee.hibernate.entity.Assignment;

import java.util.List;

/**
 * @author valeriali
 * @project orm-project
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssignmentMapper {

    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "submissions", ignore = true)
    @Mapping(target = "resources", ignore = true)
    Assignment toEntity(AssignmentRequest request);

    @Mapping(target = "lessonId", source = "lesson.id")
    @Mapping(target = "moduleId", expression = "java(assignment.getLesson() != null && assignment.getLesson().getModule() != null ? assignment.getLesson().getModule().getId() : null)")
    @Mapping(target = "courseId", expression = "java(assignment.getLesson() != null && assignment.getLesson().getModule() != null && assignment.getLesson().getModule().getCourse() != null ? assignment.getLesson().getModule().getCourse().getId() : null)")
    AssignmentResponse toResponse(Assignment assignment);

    List<AssignmentResponse> toResponseList(List<Assignment> assignments);

    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "submissions", ignore = true)
    @Mapping(target = "resources", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(AssignmentRequest request, @MappingTarget Assignment assignment);
}

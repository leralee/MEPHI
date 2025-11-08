package ru.leralee.hibernate.mapper;

import org.mapstruct.*;
import ru.leralee.hibernate.dto.nested.CourseInfo;
import ru.leralee.hibernate.dto.request.LessonRequest;
import ru.leralee.hibernate.dto.response.LessonResponse;
import ru.leralee.hibernate.entity.Lesson;
import ru.leralee.hibernate.entity.learning_resources.LearningResource;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LessonMapper {

    @Mapping(target = "module", ignore = true)
    @Mapping(target = "primaryResource", source = "primaryResourceId", qualifiedByName = "resourceIdToResource")
    Lesson toEntity(LessonRequest request);

    @Mapping(target = "course", expression = "java(courseInfo(lesson))")
    @Mapping(target = "moduleId", expression = "java(lesson.getModule() != null ? lesson.getModule().getId() : null)")
    LessonResponse toResponse(Lesson lesson);

    List<LessonResponse> toResponseList(List<Lesson> lessons);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "module", ignore = true)
    void updateEntityFromRequest(LessonRequest request, @MappingTarget Lesson lesson);

    default CourseInfo courseInfo(Lesson lesson) {
        if (lesson == null || lesson.getModule() == null || lesson.getModule().getCourse() == null) {
            return null;
        }
        CourseInfo info = new CourseInfo();
        info.setId(lesson.getModule().getCourse().getId());
        info.setCode(lesson.getModule().getCourse().getCode());
        info.setTitle(lesson.getModule().getCourse().getTitle());
        return info;
    }

    @Named("resourceIdToResource")
    default LearningResource resourceIdToResource(UUID resourceId) {
        if (resourceId == null) {
            return null;
        }
        return null;
    }
}

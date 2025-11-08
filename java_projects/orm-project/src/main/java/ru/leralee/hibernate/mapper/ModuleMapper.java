package ru.leralee.hibernate.mapper;

import org.mapstruct.*;
import ru.leralee.hibernate.dto.nested.CourseInfo;
import ru.leralee.hibernate.dto.request.ModuleRequest;
import ru.leralee.hibernate.dto.response.ModuleResponse;
import ru.leralee.hibernate.entity.Course;
import ru.leralee.hibernate.entity.CourseModule;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModuleMapper {

    @Mapping(target = "course", source = "courseId", qualifiedByName = "courseIdToCourse")
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "quiz", ignore = true)
    CourseModule toEntity(ModuleRequest request);

    @Mapping(target = "course", source = "course", qualifiedByName = "courseToInfo")
    @Mapping(target = "lessons", ignore = true)
    ModuleResponse toResponse(CourseModule module);

    List<ModuleResponse> toResponseList(List<CourseModule> modules);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "quiz", ignore = true)
    void updateEntityFromRequest(ModuleRequest request, @MappingTarget CourseModule module);

    @Named("courseIdToCourse")
    default Course courseIdToCourse(UUID courseId) {
        if (courseId == null) {
            return null;
        }
        Course course = new Course();
        course.setId(courseId);
        return course;
    }

    @Named("courseToInfo")
    default CourseInfo courseToInfo(Course course) {
        if (course == null) {
            return null;
        }
        CourseInfo info = new CourseInfo();
        info.setId(course.getId());
        info.setCode(course.getCode());
        info.setTitle(course.getTitle());
        return info;
    }
}

package ru.leralee.hibernate.mapper;

import org.mapstruct.*;
import ru.leralee.hibernate.dto.nested.AuthorInfo;
import ru.leralee.hibernate.dto.nested.CategoryInfo;
import ru.leralee.hibernate.dto.request.CourseRequest;
import ru.leralee.hibernate.dto.response.CourseResponse;
import ru.leralee.hibernate.dto.response.CourseShortResponse;
import ru.leralee.hibernate.entity.Course;
import ru.leralee.hibernate.entity.learning_resources.LearningResource;
import ru.leralee.hibernate.entity.person.Teacher;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {


    @Mapping(target = "author", source = "authorId", qualifiedByName = "authorIdToTeacher")
    @Mapping(target = "featuredResource", source = "featuredResourceId", qualifiedByName = "resourceIdToResource")
    Course toEntity(CourseRequest request);

    @Mapping(target = "author", source = "author", qualifiedByName = "teacherToAuthorInfo")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryToInfo")
    CourseResponse toResponse(Course course);

    CourseShortResponse toShortResponse(Course course);

    List<CourseResponse> toResponseList(List<Course> courses);

    List<CourseShortResponse> toShortResponseList(List<Course> courses);

    void updateEntityFromRequest(CourseRequest request, @MappingTarget Course course);

    @Named("teacherToAuthorInfo")
    default AuthorInfo teacherToAuthorInfo(Teacher teacher) {
        if (teacher == null) {
            return null;
        }
        AuthorInfo info = new AuthorInfo();
        info.setId(teacher.getId());
        info.setFirstName(teacher.getFirstName());
        info.setLastName(teacher.getLastName());
        info.setAcademicTitle(teacher.getAcademicTitle());
        return info;
    }

    @Named("authorIdToTeacher")
    default Teacher authorIdToTeacher(UUID authorId) {
        if (authorId == null) {
            return null;
        }
        Teacher teacher = new Teacher();
        teacher.setId(authorId);
        return teacher;
    }

    @Named("resourceIdToResource")
    default LearningResource resourceIdToResource(UUID resourceId) {
        if (resourceId == null) {
            return null;
        }
        return null;
    }

    @Named("categoryToInfo")
    default CategoryInfo categoryToInfo(ru.leralee.hibernate.entity.Category category) {
        if (category == null) {
            return null;
        }
        CategoryInfo info = new CategoryInfo();
        info.setId(category.getId());
        info.setName(category.getName());
        info.setSlug(category.getSlug());
        return info;
    }
}

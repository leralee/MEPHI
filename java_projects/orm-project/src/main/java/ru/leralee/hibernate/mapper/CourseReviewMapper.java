package ru.leralee.hibernate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.leralee.hibernate.dto.request.CourseReviewRequest;
import ru.leralee.hibernate.dto.response.CourseReviewResponse;
import ru.leralee.hibernate.entity.CourseReview;

import java.util.List;

/**
 * @author valeriali
 * @project orm-project
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseReviewMapper {

    @Mapping(target = "course", ignore = true)
    @Mapping(target = "student", ignore = true)
    CourseReview toEntity(CourseReviewRequest request);

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "studentId", source = "student.id")
    CourseReviewResponse toResponse(CourseReview review);

    List<CourseReviewResponse> toResponseList(List<CourseReview> reviews);

    @Mapping(target = "course", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(CourseReviewRequest request, @MappingTarget CourseReview review);
}

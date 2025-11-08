package ru.leralee.hibernate.mapper;

import org.mapstruct.*;
import ru.leralee.hibernate.dto.request.TeacherRequest;
import ru.leralee.hibernate.dto.response.TeacherResponse;
import ru.leralee.hibernate.entity.person.Teacher;

import java.util.List;

/**
 * @author valeriali
 * @project orm-project
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeacherMapper {

    Teacher toEntity(TeacherRequest request);

    TeacherResponse toResponse(Teacher teacher);

    List<TeacherResponse> toResponseList(List<Teacher> teachers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(TeacherRequest request, @MappingTarget Teacher teacher);
}

package ru.leralee.hibernate.mapper;

import org.mapstruct.*;
import ru.leralee.hibernate.dto.request.TagRequest;
import ru.leralee.hibernate.dto.response.TagResponse;
import ru.leralee.hibernate.dto.response.TagShortResponse;
import ru.leralee.hibernate.entity.Tag;

import java.util.List;

/**
 * @author valeriali
 * @project orm-project
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "courses", ignore = true)
    Tag toEntity(TagRequest request);

    TagResponse toResponse(Tag tag);

    TagShortResponse toShortResponse(Tag tag);

    List<TagResponse> toResponseList(List<Tag> tags);

    List<TagShortResponse> toShortResponseList(List<Tag> tags);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "courses", ignore = true)
    void updateEntityFromRequest(TagRequest request, @MappingTarget Tag tag);

    @AfterMapping
    default void afterToEntity(@MappingTarget Tag tag, TagRequest request) {
        if (tag.getSlug() == null || tag.getSlug().isEmpty()) {
            tag.setSlug(request.getName().toLowerCase().replaceAll("\\s+", "-"));
        }
    }
}

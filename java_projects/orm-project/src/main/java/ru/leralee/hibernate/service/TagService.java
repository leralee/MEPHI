package ru.leralee.hibernate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leralee.hibernate.dto.request.TagRequest;
import ru.leralee.hibernate.dto.response.CourseShortResponse;
import ru.leralee.hibernate.dto.response.TagResponse;
import ru.leralee.hibernate.dto.response.TagShortResponse;
import ru.leralee.hibernate.entity.Tag;
import ru.leralee.hibernate.exception.AppException;
import ru.leralee.hibernate.exception.NotFoundException;
import ru.leralee.hibernate.mapper.TagMapper;
import ru.leralee.hibernate.repository.TagRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author valeriali
 * @project orm-project
 */
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final CourseService courseService;

    @Transactional
    public TagResponse create(TagRequest request) {
        tagRepository.findBySlug(request.getSlug()).ifPresent(tag -> {
            throw new AppException("Tag with slug '" + request.getSlug() + "' already exists", 409);
        });

        Tag tag = tagMapper.toEntity(request);
        Tag saved = tagRepository.save(tag);
        return tagMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TagResponse findById(UUID id) {
        Tag tag = getTag(id);
        return tagMapper.toResponse(tag);
    }

    @Transactional(readOnly = true)
    public TagResponse findBySlug(String slug) {
        Tag tag = tagRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException("Tag not found with slug: " + slug, 404));
        return tagMapper.toResponse(tag);
    }

    @Transactional(readOnly = true)
    public List<TagResponse> findAll() {
        List<Tag> tags = tagRepository.findAll();
        return tagMapper.toResponseList(tags);
    }

    @Transactional(readOnly = true)
    public List<TagShortResponse> findAllShort() {
        List<Tag> tags = tagRepository.findAll();
        return tagMapper.toShortResponseList(tags);
    }

    @Transactional(readOnly = true)
    public List<CourseShortResponse> getTagCourses(UUID tagId) {
        Tag tag = getTag(tagId);

        return tag.getCourses().stream()
                .map(course -> courseService.findById(course.getId()))
                .map(courseResponse -> {
                    CourseShortResponse shortResponse = new CourseShortResponse();
                    shortResponse.setId(courseResponse.getId());
                    shortResponse.setCode(courseResponse.getCode());
                    shortResponse.setTitle(courseResponse.getTitle());
                    shortResponse.setCredits(courseResponse.getCredits());
                    return shortResponse;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public TagResponse update(UUID id, TagRequest request) {
        Tag tag = getTag(id);
        tagMapper.updateEntityFromRequest(request, tag);

        Tag updated = tagRepository.save(tag);
        return tagMapper.toResponse(updated);
    }

    @Transactional
    public void delete(UUID id) {
        Tag tag = getTag(id);

        if (!tag.getCourses().isEmpty()) {
            throw new AppException("Cannot delete tag with associated courses. Found " + tag.getCourses().size() + " courses.", 409);
        }

        tagRepository.deleteById(id);
    }

    @Transactional
    public void forceDelete(UUID id) {
        Tag tag = getTag(id);

        tag.getCourses().forEach(course -> {
            course.getTags().remove(tag);
        });
        tag.getCourses().clear();

        tagRepository.deleteById(id);
    }

    private Tag getTag(UUID id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + id));
    }
}

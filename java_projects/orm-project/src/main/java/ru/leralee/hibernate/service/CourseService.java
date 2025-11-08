package ru.leralee.hibernate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leralee.hibernate.dto.request.CourseRequest;
import ru.leralee.hibernate.dto.request.LessonRequest;
import ru.leralee.hibernate.dto.response.CourseResponse;
import ru.leralee.hibernate.dto.response.CourseShortResponse;
import ru.leralee.hibernate.dto.response.LessonResponse;
import ru.leralee.hibernate.dto.response.TagShortResponse;
import ru.leralee.hibernate.entity.Category;
import ru.leralee.hibernate.entity.Course;
import ru.leralee.hibernate.entity.Tag;
import ru.leralee.hibernate.entity.person.Teacher;
import ru.leralee.hibernate.exception.NotFoundException;
import ru.leralee.hibernate.mapper.CourseMapper;
import ru.leralee.hibernate.repository.CategoryRepository;
import ru.leralee.hibernate.repository.CourseRepository;
import ru.leralee.hibernate.repository.TagRepository;
import ru.leralee.hibernate.repository.TeacherRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author valeriali
 * @project orm-project
 */
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final CourseMapper courseMapper;
    @Autowired @Lazy private TagService tagService;
    private final LessonService lessonService;

    @Transactional
    public CourseResponse create(CourseRequest request) {
        Teacher author = teacherRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Teacher not found with id: " + request.getAuthorId()));

        Course course = courseMapper.toEntity(request);
        course.setAuthor(author);
        if (request.getCategoryId() != null) {
            course.setCategory(getCategory(request.getCategoryId()));
        }

        Course saved = courseRepository.save(course);
        return courseMapper.toResponse(saved);
    }

    @Transactional
    public CourseResponse createWithTags(CourseRequest request, List<UUID> tagIds) {
        CourseResponse courseResponse = create(request);

        for (UUID tagId : tagIds) {
            addTagToCourse(courseResponse.getId(), tagId);
        }

        return findById(courseResponse.getId());
    }

    @Transactional(readOnly = true)
    public CourseResponse findById(UUID id) {
        Course course = getCourse(id);
        return courseMapper.toResponse(course);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> findAll() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toResponseList(courses);
    }

    @Transactional(readOnly = true)
    public List<CourseShortResponse> findAllShort() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toShortResponseList(courses);
    }

    @Transactional
    public CourseResponse update(UUID id, CourseRequest request) {
        Course course = getCourse(id);

        if (request.getAuthorId() != null && !request.getAuthorId().equals(course.getAuthor().getId())) {
            Teacher newAuthor = teacherRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new NotFoundException("Teacher not found with id: " + request.getAuthorId()));
            course.setAuthor(newAuthor);
        }

        if (request.getCategoryId() != null) {
            course.setCategory(getCategory(request.getCategoryId()));
        }

        courseMapper.updateEntityFromRequest(request, course);

        Course updated = courseRepository.save(course);
        return courseMapper.toResponse(updated);
    }

    @Transactional
    public void addKeyword(UUID courseId, String keyword) {
        Course course = getCourse(courseId);
        course.getKeywords().add(keyword);
    }

    @Transactional
    public void removeKeyword(UUID courseId, String keyword) {
        Course course = getCourse(courseId);
        course.getKeywords().remove(keyword);
    }

    @Transactional
    public void setKeywords(UUID courseId, Set<String> keywords) {
        Course course = getCourse(courseId);
        course.getKeywords().clear();
        course.getKeywords().addAll(keywords);
    }

    @Transactional
    public void addTagToCourse(UUID courseId, UUID tagId) {
        Course course = getCourse(courseId);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + tagId));
        course.getTags().add(tag);
    }

    @Transactional
    public void addTagBySLug(UUID courseId, String slug) {
        Course course = getCourse(courseId);
        Tag tag = tagRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Tag not found with slug: " + slug));
        course.getTags().add(tag);
    }

    @Transactional
    public void removeTagFromCourse(UUID courseId, UUID tagId) {
        Course course = getCourse(courseId);
        course.getTags().removeIf(tag -> tag.getId().equals(tagId));
    }

    @Transactional(readOnly = true)
    public List<TagShortResponse> getCourseTagsShort(UUID courseId) {
        Course course = getCourse(courseId);
        List<UUID> tagIds = course.getTags().stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        return tagIds.stream()
                .map(tagService::findById)
                .map(tagResponse -> {
                    TagShortResponse shortResponse = new TagShortResponse();
                    shortResponse.setId(tagResponse.getId());
                    shortResponse.setName(tagResponse.getName());
                    shortResponse.setSlug(tagResponse.getSlug());
                    return shortResponse;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public LessonResponse addLessonToCourse(UUID courseId, LessonRequest lessonRequest) {
        getCourse(courseId);

        lessonRequest.setCourseId(courseId);
        return lessonService.create(lessonRequest);
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> getCourseLessons(UUID courseId) {
        return lessonService.findByCourseId(courseId);
    }

    @Transactional
    public void delete(UUID id) {
        courseRepository.deleteById(id);
    }

    private Course getCourse(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + id));
    }

    private Category getCategory(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
    }
}

package ru.leralee.hibernate.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.leralee.hibernate.dto.request.CourseRequest;
import ru.leralee.hibernate.dto.request.LessonRequest;
import ru.leralee.hibernate.dto.response.*;
import ru.leralee.hibernate.service.CourseService;
import ru.leralee.hibernate.service.LessonService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@RestController
@RequestMapping("/api/courses")
@Validated
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final LessonService lessonService;

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping
    public CourseResponse create(@Valid @RequestBody CourseRequest request) {
        return courseService.create(request);
    }

    @GetMapping
    public List<CourseResponse> findAll() {
        return courseService.findAll();
    }

    @GetMapping("/short")
    public List<CourseShortResponse> findAllShort() {
        return courseService.findAllShort();
    }

    @GetMapping("/{id}")
    public CourseResponse findById(@PathVariable UUID id) {
        return courseService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{id}")
    public CourseResponse update(@PathVariable UUID id, @Valid @RequestBody CourseRequest request) {
        return courseService.update(id, request);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping("/{id}/keywords")
    public void addKeyword(@PathVariable UUID id, @RequestParam @NotBlank String keyword) {
        courseService.addKeyword(id, keyword);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @DeleteMapping("/{id}/keywords")
    public void removeKeyword(@PathVariable UUID id, @RequestParam @NotBlank String keyword) {
        courseService.removeKeyword(id, keyword);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{id}/keywords")
    public void setKeywords(@PathVariable UUID id, @RequestBody Set<String> keywords) {
        courseService.setKeywords(id, keywords);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping("/{courseId}/tags/{tagId}")
    public void addTag(@PathVariable UUID courseId, @PathVariable UUID tagId) {
        courseService.addTagToCourse(courseId, tagId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @DeleteMapping("/{courseId}/tags/{tagId}")
    public void removeTag(@PathVariable UUID courseId, @PathVariable UUID tagId) {
        courseService.removeTagFromCourse(courseId, tagId);
    }

    @GetMapping("/{courseId}/tags")
    public List<TagShortResponse> getTags(@PathVariable UUID courseId) {
        return courseService.getCourseTagsShort(courseId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping("/{courseId}/lessons")
    public LessonResponse addLesson(@PathVariable UUID courseId, @Valid @RequestBody LessonRequest request) {
        request.setCourseId(courseId);
        return courseService.addLessonToCourse(courseId, request);
    }

    @GetMapping("/{courseId}/lessons")
    public List<LessonResponse> getLessons(@PathVariable UUID courseId) {
        return lessonService.findByCourseId(courseId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        courseService.delete(id);
    }
}

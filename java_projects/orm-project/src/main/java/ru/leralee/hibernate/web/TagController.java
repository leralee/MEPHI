package ru.leralee.hibernate.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.leralee.hibernate.dto.request.TagRequest;
import ru.leralee.hibernate.dto.response.CourseShortResponse;
import ru.leralee.hibernate.dto.response.TagResponse;
import ru.leralee.hibernate.dto.response.TagShortResponse;
import ru.leralee.hibernate.service.TagService;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@RestController
@RequestMapping("/api/tags")
@Validated
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public TagResponse create(@Valid @RequestBody TagRequest request) {
        return tagService.create(request);
    }

    @GetMapping
    public List<TagResponse> findAll() {
        return tagService.findAll();
    }

    @GetMapping("/short")
    public List<TagShortResponse> findAllShort() {
        return tagService.findAllShort();
    }

    @GetMapping("/{id}")
    public TagResponse findById(@PathVariable UUID id) {
        return tagService.findById(id);
    }

    @GetMapping("/{id}/courses")
    public List<CourseShortResponse> getCourses(@PathVariable UUID id) {
        return tagService.getTagCourses(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public TagResponse update(@PathVariable UUID id, @Valid @RequestBody TagRequest request) {
        return tagService.update(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        tagService.delete(id);
    }
}

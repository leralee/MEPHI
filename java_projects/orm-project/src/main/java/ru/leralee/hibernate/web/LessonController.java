package ru.leralee.hibernate.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.leralee.hibernate.dto.request.LessonRequest;
import ru.leralee.hibernate.dto.response.LessonResponse;
import ru.leralee.hibernate.service.LessonService;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@RestController
@RequestMapping("/api/lessons")
@Validated
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping
    public LessonResponse create(@Valid @RequestBody LessonRequest request) {
        return lessonService.create(request);
    }

    @GetMapping("/{id}")
    public LessonResponse findById(@PathVariable UUID id) {
        return lessonService.findById(id);
    }

    @GetMapping
    public List<LessonResponse> findAll() {
        return lessonService.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{id}")
    public LessonResponse update(@PathVariable UUID id, @Valid @RequestBody LessonRequest request) {
        return lessonService.update(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        lessonService.delete(id);
    }
}

package ru.leralee.hibernate.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.leralee.hibernate.dto.request.AssignmentRequest;
import ru.leralee.hibernate.dto.response.AssignmentResponse;
import ru.leralee.hibernate.service.AssignmentService;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@RestController
@RequestMapping("/api/assignments")
@Validated
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping
    public AssignmentResponse create(@Valid @RequestBody AssignmentRequest request) {
        return assignmentService.create(request);
    }

    @GetMapping("/{id}")
    public AssignmentResponse findById(@PathVariable UUID id) {
        return assignmentService.findById(id);
    }

    @GetMapping("/lesson/{lessonId}")
    public List<AssignmentResponse> byLesson(@PathVariable UUID lessonId) {
        return assignmentService.findByLesson(lessonId);
    }

    @GetMapping("/course/{courseId}")
    public List<AssignmentResponse> byCourse(@PathVariable UUID courseId) {
        return assignmentService.findByCourse(courseId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{id}")
    public AssignmentResponse update(@PathVariable UUID id, @Valid @RequestBody AssignmentRequest request) {
        return assignmentService.update(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        assignmentService.delete(id);
    }
}

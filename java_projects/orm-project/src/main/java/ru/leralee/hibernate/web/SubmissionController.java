package ru.leralee.hibernate.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.leralee.hibernate.dto.request.SubmissionRequest;
import ru.leralee.hibernate.dto.response.SubmissionResponse;
import ru.leralee.hibernate.service.SubmissionService;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@RestController
@RequestMapping("/api/submissions")
@Validated
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    public SubmissionResponse submit(@Valid @RequestBody SubmissionRequest request) {
        return submissionService.submit(request);
    }

    @GetMapping("/{id}")
    public SubmissionResponse findById(@PathVariable UUID id) {
        return submissionService.findById(id);
    }

    @GetMapping("/assignments/{assignmentId}")
    public List<SubmissionResponse> findByAssignment(@PathVariable UUID assignmentId) {
        return submissionService.findByAssignment(assignmentId);
    }

    @GetMapping("/students/{studentId}")
    public List<SubmissionResponse> findByStudent(@PathVariable UUID studentId) {
        return submissionService.findByStudent(studentId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping("/{id}/grade")
    public SubmissionResponse grade(@PathVariable UUID id,
                                    @RequestParam Double score,
                                    @RequestParam(required = false) String feedback) {
        return submissionService.grade(id, score, feedback);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{id}")
    public SubmissionResponse update(@PathVariable UUID id, @Valid @RequestBody SubmissionRequest request) {
        return submissionService.update(id, request);
    }
}

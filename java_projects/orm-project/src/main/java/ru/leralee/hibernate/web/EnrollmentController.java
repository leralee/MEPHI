package ru.leralee.hibernate.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.leralee.hibernate.dto.request.EnrollmentRequest;
import ru.leralee.hibernate.dto.response.CourseShortResponse;
import ru.leralee.hibernate.dto.response.EnrollmentResponse;
import ru.leralee.hibernate.dto.response.StudentResponse;
import ru.leralee.hibernate.enums.EnrollmentRole;
import ru.leralee.hibernate.service.EnrollmentService;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@RestController
@RequestMapping("/api/enrollments")
@Validated
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping("/courses/{courseId}/students/{studentId}")
    public EnrollmentResponse enroll(@PathVariable UUID courseId,
                                     @PathVariable UUID studentId,
                                     @RequestParam(defaultValue = "STUDENT") EnrollmentRole role) {
        return enrollmentService.enrollStudent(studentId, courseId, role);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping
    public EnrollmentResponse create(@Valid @RequestBody EnrollmentRequest request) {
        return enrollmentService.create(request);
    }

    @GetMapping
    public List<EnrollmentResponse> findAll() {
        return enrollmentService.findAll();
    }

    @GetMapping("/students/{studentId}")
    public List<EnrollmentResponse> byStudent(@PathVariable UUID studentId) {
        return enrollmentService.findByStudentId(studentId);
    }

    @GetMapping("/courses/{courseId}")
    public List<EnrollmentResponse> byCourse(@PathVariable UUID courseId) {
        return enrollmentService.findByCourseId(courseId);
    }

    @GetMapping("/students/{studentId}/courses")
    public List<CourseShortResponse> studentCourses(@PathVariable UUID studentId) {
        return enrollmentService.getStudentCourses(studentId);
    }

    @GetMapping("/courses/{courseId}/students")
    public List<StudentResponse> courseStudents(@PathVariable UUID courseId) {
        return enrollmentService.getCourseStudents(courseId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{id}")
    public EnrollmentResponse update(@PathVariable UUID id, @Valid @RequestBody EnrollmentRequest request) {
        return enrollmentService.update(id, request);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping("/{id}/complete")
    public void complete(@PathVariable UUID id, @RequestParam Double finalGrade) {
        enrollmentService.completeEnrollment(id, finalGrade);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping("/{id}/drop")
    public void drop(@PathVariable UUID id) {
        enrollmentService.dropEnrollment(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        enrollmentService.delete(id);
    }
}

package ru.leralee.hibernate.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.leralee.hibernate.dto.request.CourseReviewRequest;
import ru.leralee.hibernate.dto.response.CourseReviewResponse;
import ru.leralee.hibernate.service.CourseReviewService;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@RestController
@RequestMapping("/api/reviews")
@Validated
@RequiredArgsConstructor
public class CourseReviewController {

    private final CourseReviewService courseReviewService;

    @PostMapping
    public CourseReviewResponse create(@Valid @RequestBody CourseReviewRequest request) {
        return courseReviewService.create(request);
    }

    @GetMapping("/courses/{courseId}")
    public List<CourseReviewResponse> getCourseReviews(@PathVariable UUID courseId) {
        return courseReviewService.getCourseReviews(courseId);
    }

    @PutMapping("/{id}")
    public CourseReviewResponse update(@PathVariable UUID id, @Valid @RequestBody CourseReviewRequest request) {
        return courseReviewService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        courseReviewService.delete(id);
    }
}

package ru.leralee.hibernate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leralee.hibernate.dto.request.CourseReviewRequest;
import ru.leralee.hibernate.dto.response.CourseReviewResponse;
import ru.leralee.hibernate.entity.Course;
import ru.leralee.hibernate.entity.CourseReview;
import ru.leralee.hibernate.entity.person.Student;
import ru.leralee.hibernate.exception.NotFoundException;
import ru.leralee.hibernate.mapper.CourseReviewMapper;
import ru.leralee.hibernate.repository.CourseRepository;
import ru.leralee.hibernate.repository.CourseReviewRepository;
import ru.leralee.hibernate.repository.StudentRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Service
@RequiredArgsConstructor
public class CourseReviewService {

    private final CourseReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final CourseReviewMapper reviewMapper;

    @Transactional
    public CourseReviewResponse create(CourseReviewRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + request.getCourseId()));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + request.getStudentId()));

        CourseReview review = reviewMapper.toEntity(request);
        review.setCourse(course);
        review.setStudent(student);

        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public List<CourseReviewResponse> getCourseReviews(UUID courseId) {
        return reviewMapper.toResponseList(reviewRepository.findByCourseId(courseId));
    }

    @Transactional
    public CourseReviewResponse update(UUID id, CourseReviewRequest request) {
        CourseReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + id));
        reviewMapper.updateEntityFromRequest(request, review);
        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Transactional
    public void delete(UUID id) {
        reviewRepository.deleteById(id);
    }
}

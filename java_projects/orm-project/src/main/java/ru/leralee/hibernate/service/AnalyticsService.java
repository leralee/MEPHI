package ru.leralee.hibernate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.leralee.hibernate.dto.response.AnalyticsSummary;
import ru.leralee.hibernate.repository.*;

/**
 * @author valeriali
 * @project orm-project
 */
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AssignmentRepository assignmentRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final CourseReviewRepository courseReviewRepository;

    public AnalyticsSummary getSummary() {
        long totalCourses = courseRepository.count();
        long totalStudents = studentRepository.count();
        long totalTeachers = teacherRepository.count();
        long totalAssignments = assignmentRepository.count();
        long totalQuizSubmissions = quizSubmissionRepository.count();
        double averageRating = courseReviewRepository.findAll().stream()
                .mapToInt(review -> review.getRating() != null ? review.getRating() : 0)
                .filter(rating -> rating > 0)
                .average()
                .orElse(0.0);

        return AnalyticsSummary.builder()
                .totalCourses(totalCourses)
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalAssignments(totalAssignments)
                .totalQuizSubmissions(totalQuizSubmissions)
                .averageCourseRating(averageRating)
                .build();
    }
}

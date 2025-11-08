package ru.leralee.hibernate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leralee.hibernate.dto.request.EnrollmentRequest;
import ru.leralee.hibernate.dto.response.CourseShortResponse;
import ru.leralee.hibernate.dto.response.EnrollmentResponse;
import ru.leralee.hibernate.dto.response.StudentResponse;
import ru.leralee.hibernate.entity.Course;
import ru.leralee.hibernate.entity.Enrollment;
import ru.leralee.hibernate.entity.person.Student;
import ru.leralee.hibernate.enums.EnrollmentRole;
import ru.leralee.hibernate.enums.EnrollmentStatus;
import ru.leralee.hibernate.enums.StudentStatus;
import ru.leralee.hibernate.exception.AppException;
import ru.leralee.hibernate.exception.NotFoundException;
import ru.leralee.hibernate.mapper.EnrollmentMapper;
import ru.leralee.hibernate.repository.CourseRepository;
import ru.leralee.hibernate.repository.EnrollmentRepository;
import ru.leralee.hibernate.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author valeriali
 * @project orm-project
 */
@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final UserService userService;
    private final CourseService courseService;

    @Transactional
    public EnrollmentResponse enrollStudent(UUID studentId, UUID courseId, EnrollmentRole role) {
        StudentResponse studentResponse = userService.findStudentById(studentId);

        if (studentResponse.getStatus() != StudentStatus.ACTIVE) {
            throw new AppException("Student is not active", 400);
        }

        courseService.findById(courseId);

        enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).ifPresent(e -> {
            throw new AppException("Student is already enrolled in this course", 409);
        });

        Student student = getStudent(studentId);
        Course course = getCourse(courseId);

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setRole(role);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.ENROLLED);

        Enrollment saved = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(saved);
    }

    @Transactional
    public EnrollmentResponse create(EnrollmentRequest request) {
        userService.findStudentById(request.getStudentId());
        courseService.findById(request.getCourseId());

        enrollmentRepository.findByStudentIdAndCourseId(request.getStudentId(), request.getCourseId())
                .ifPresent(e -> {
                    throw new AppException("Enrollment already exists", 409);
                });

        Student student = getStudent(request.getStudentId());
        Course course = getCourse(request.getCourseId());

        Enrollment enrollment = enrollmentMapper.toEntity(request);
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        Enrollment saved = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public EnrollmentResponse findById(UUID id) {
        Enrollment enrollment = getEnrollment(id);
        return enrollmentMapper.toResponse(enrollment);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findAll() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        return enrollmentMapper.toResponseList(enrollments);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByStudentId(UUID studentId) {
        userService.findStudentById(studentId);

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollmentMapper.toResponseList(enrollments);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByCourseId(UUID courseId) {
        courseService.findById(courseId);

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        return enrollmentMapper.toResponseList(enrollments);
    }

    @Transactional(readOnly = true)
    public List<CourseShortResponse> getStudentCourses(UUID studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        List<UUID> courseIds = enrollments.stream()
                .map(e -> e.getCourse().getId())
                .collect(Collectors.toList());

        return courseIds.stream()
                .map(courseService::findById)
                .map(courseResponse -> {
                    CourseShortResponse shortResponse = new CourseShortResponse();
                    shortResponse.setId(courseResponse.getId());
                    shortResponse.setCode(courseResponse.getCode());
                    shortResponse.setTitle(courseResponse.getTitle());
                    shortResponse.setCredits(courseResponse.getCredits());
                    return shortResponse;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getCourseStudents(UUID courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

        List<UUID> studentIds = enrollments.stream()
                .map(e -> e.getStudent().getId())
                .collect(Collectors.toList());

        return studentIds.stream()
                .map(userService::findStudentById)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentResponse update(UUID id, EnrollmentRequest request) {
        Enrollment enrollment = getEnrollment(id);
        enrollmentMapper.updateEntityFromRequest(request, enrollment);

        Enrollment updated = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(updated);
    }

    @Transactional
    public void completeEnrollment(UUID enrollmentId, Double finalGrade) {
        Enrollment enrollment = getEnrollment(enrollmentId);
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollment.setFinalGrade(finalGrade);
    }

    @Transactional
    public void dropEnrollment(UUID enrollmentId) {
        Enrollment enrollment = getEnrollment(enrollmentId);
        enrollment.setStatus(EnrollmentStatus.DROPPED);
    }

    @Transactional
    public void delete(UUID id) {
        enrollmentRepository.deleteById(id);
    }

    private Student getStudent(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + id));
    }

    private Course getCourse(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + id));
    }

    private Enrollment getEnrollment(UUID id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enrollment not found with id: " + id));
    }
}

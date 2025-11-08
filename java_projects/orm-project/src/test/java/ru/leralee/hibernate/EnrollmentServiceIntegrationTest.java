package ru.leralee.hibernate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.leralee.hibernate.dto.response.CourseShortResponse;
import ru.leralee.hibernate.dto.response.EnrollmentResponse;
import ru.leralee.hibernate.dto.response.StudentResponse;
import ru.leralee.hibernate.entity.Category;
import ru.leralee.hibernate.entity.Course;
import ru.leralee.hibernate.entity.person.Student;
import ru.leralee.hibernate.entity.person.Teacher;
import ru.leralee.hibernate.enums.EnrollmentRole;
import ru.leralee.hibernate.enums.StudentStatus;
import ru.leralee.hibernate.enums.UserRole;
import ru.leralee.hibernate.exception.AppException;
import ru.leralee.hibernate.repository.*;
import ru.leralee.hibernate.service.EnrollmentService;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnrollmentServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Teacher teacher;
    private Student student;
    private Course course;

    @BeforeEach
    void initData() {
        teacher = new Teacher();
        teacher.setFirstName("Eva");
        teacher.setLastName("Mentor");
        teacher.setEmail("eva." + UUID.randomUUID() + "@example.com");
        teacher.setRole(UserRole.TEACHER);
        teacherRepository.save(teacher);

        student = new Student();
        student.setFirstName("Oliver");
        student.setLastName("Student");
        student.setEmail("oliver." + UUID.randomUUID() + "@example.com");
        student.setStudentNo("STU" + UUID.randomUUID().toString().substring(0, 5));
        student.setStatus(StudentStatus.ACTIVE);
        student.setRole(UserRole.STUDENT);
        studentRepository.save(student);

        Category category = new Category();
        category.setName("Databases" + UUID.randomUUID());
        category.setSlug("db" + UUID.randomUUID());
        categoryRepository.save(category);

        course = new Course();
        course.setAuthor(teacher);
        course.setCategory(category);
        course.setCode("DB-" + UUID.randomUUID());
        course.setTitle("DB Secrets");
        courseRepository.save(course);
    }

    @Test
    void shouldEnrollStudentOnlyOnce() {
        EnrollmentResponse response = enrollmentService.enrollStudent(student.getId(), course.getId(), EnrollmentRole.STUDENT);
        assertThat(response.getId()).isNotNull();

        assertThatThrownBy(() -> enrollmentService.enrollStudent(student.getId(), course.getId(), EnrollmentRole.STUDENT))
                .isInstanceOf(AppException.class)
                .hasMessageContaining("already enrolled");
    }

    @Test
    void shouldReturnStudentCourses() {
        enrollmentService.enrollStudent(student.getId(), course.getId(), EnrollmentRole.STUDENT);

        List<CourseShortResponse> courses = enrollmentService.getStudentCourses(student.getId());
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getTitle()).isEqualTo("DB Secrets");

        List<StudentResponse> students = enrollmentService.getCourseStudents(course.getId());
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getEmail()).isEqualTo(student.getEmail());
    }
}

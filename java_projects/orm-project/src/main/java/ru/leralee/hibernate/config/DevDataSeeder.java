package ru.leralee.hibernate.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.leralee.hibernate.entity.*;
import ru.leralee.hibernate.entity.person.Student;
import ru.leralee.hibernate.entity.person.Teacher;
import ru.leralee.hibernate.enums.StudentStatus;
import ru.leralee.hibernate.enums.UserRole;
import ru.leralee.hibernate.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataSeeder implements CommandLineRunner {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final CourseRepository courseRepository;
    private final CourseModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final AssignmentRepository assignmentRepository;

    @Override
    public void run(String... args) {
        if (courseRepository.count() > 0) {
            return;
        }

        Teacher teacher = new Teacher();
        teacher.setFirstName("Dana");
        teacher.setLastName("Curie");
        teacher.setAcademicTitle("Senior Lecturer");
        teacher.setEmail("dana.curie@demo.dev");
        teacher.setBirthDate(LocalDate.of(1985, 6, 15));
        teacher.setRole(UserRole.TEACHER);
        teacherRepository.save(teacher);

        Student student = new Student();
        student.setFirstName("Leo");
        student.setLastName("Connor");
        student.setEmail("leo.connor@demo.dev");
        student.setBirthDate(LocalDate.of(2001, 2, 11));
        student.setStudentNo("DEV001");
        student.setStatus(StudentStatus.ACTIVE);
        student.setRole(UserRole.STUDENT);
        student.getEmails().add("leo.connor@demo.dev");
        studentRepository.save(student);

        Category category = new Category();
        category.setName("Software Engineering");
        category.setSlug("software-engineering");
        categoryRepository.save(category);

        Tag tag = new Tag();
        tag.setName("Spring");
        tag.setSlug("spring");
        tagRepository.save(tag);

        Course course = new Course();
        course.setAuthor(teacher);
        course.setCategory(category);
        course.setCode("DEV-SPRING");
        course.setTitle("Spring Data Fundamentals");
        course.setDescription("Быстрый старт по построению учебной платформы на Spring Boot.");
        course.setCredits(6);
        course.getKeywords().addAll(List.of("java", "spring", "jpa"));

        course.getTags().add(tag);
        tag.getCourses().add(course);

        courseRepository.save(course);

        CourseModule module = new CourseModule();
        module.setCourse(course);
        module.setTitle("Введение");
        module.setDescription("Стартовая неделя курса");
        module.setOrderIndex(1);
        moduleRepository.save(module);

        Lesson lesson = new Lesson();
        lesson.setModule(module);
        lesson.setTitle("Сборка Spring Boot приложения");
        lesson.setSyllabus("От Gradle до запуска REST-сервера");
        lesson.getResources().add("https://docs.spring.io/spring-boot/docs/current/reference/html/");
        lessonRepository.save(lesson);

        Assignment assignment = new Assignment();
        assignment.setLesson(lesson);
        assignment.setTitle("Собрать REST API");
        assignment.setDescription("Написать контроллеры для учебной платформы");
        assignment.setDueDate(LocalDateTime.now().plusDays(7));
        assignment.setMaxPoints(100);
        assignmentRepository.save(assignment);
    }
}

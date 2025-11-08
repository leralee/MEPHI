package ru.leralee.hibernate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.leralee.hibernate.dto.request.CourseRequest;
import ru.leralee.hibernate.dto.request.LessonRequest;
import ru.leralee.hibernate.dto.response.CourseResponse;
import ru.leralee.hibernate.dto.response.LessonResponse;
import ru.leralee.hibernate.entity.Category;
import ru.leralee.hibernate.entity.Course;
import ru.leralee.hibernate.entity.CourseModule;
import ru.leralee.hibernate.entity.Tag;
import ru.leralee.hibernate.entity.person.Teacher;
import ru.leralee.hibernate.enums.UserRole;
import ru.leralee.hibernate.repository.*;
import ru.leralee.hibernate.service.CourseService;
import ru.leralee.hibernate.service.LessonService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CourseServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseModuleRepository moduleRepository;
    @Autowired
    private LessonService lessonService;

    private Teacher teacher;
    private Category category;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        teacher.setEmail("teacher+" + UUID.randomUUID() + "@example.com");
        teacher.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher.setRole(UserRole.TEACHER);
        teacherRepository.save(teacher);

        category = new Category();
        category.setName("QA");
        category.setSlug("qa" + UUID.randomUUID());
        categoryRepository.save(category);
    }

    @Test
    void shouldCreateAndUpdateCourse() {
        CourseRequest createRequest = new CourseRequest();
        createRequest.setAuthorId(teacher.getId());
        createRequest.setCategoryId(category.getId());
        createRequest.setCode("QA-" + UUID.randomUUID());
        createRequest.setTitle("Quality Assurance");
        createRequest.setDescription("Initial version");
        createRequest.setKeywords(new java.util.HashSet<>(List.of("qa", "testing")));

        CourseResponse created = courseService.create(createRequest);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getAuthor().getId()).isEqualTo(teacher.getId());

        CourseRequest updateRequest = new CourseRequest();
        updateRequest.setTitle("Quality Assurance Basics");
        updateRequest.setAuthorId(teacher.getId());
        updateRequest.setCategoryId(category.getId());

        CourseResponse updated = courseService.update(created.getId(), updateRequest);
        assertThat(updated.getTitle()).isEqualTo("Quality Assurance Basics");
    }

    @Test
    void shouldManageTagsForCourse() {
        Course course = createCourseEntity();
        Tag tag = new Tag();
        tag.setName("JUnit");
        tag.setSlug("junit-" + UUID.randomUUID());
        tagRepository.save(tag);

        courseService.addTagToCourse(course.getId(), tag.getId());
        assertThat(courseService.getCourseTagsShort(course.getId()))
                .extracting("slug")
                .contains(tag.getSlug());

        courseService.removeTagFromCourse(course.getId(), tag.getId());
        assertThat(courseService.getCourseTagsShort(course.getId())).isEmpty();
    }

    @Test
    void shouldAddLessonThroughService() {
        Course course = createCourseEntity();
        CourseModule module = new CourseModule();
        module.setCourse(course);
        module.setTitle("Module A");
        module.setDescription("Module A description");
        module.setOrderIndex(1);
        moduleRepository.save(module);

        LessonRequest request = new LessonRequest();
        request.setCourseId(course.getId());
        request.setModuleId(module.getId());
        request.setTitle("Lesson A");
        request.setSyllabus("Syllabus");
        request.setResources(List.of("https://example.com"));

        LessonResponse response = courseService.addLessonToCourse(course.getId(), request);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getCourse().getId()).isEqualTo(course.getId());
        assertThat(lessonService.findByCourseId(course.getId())).hasSize(1);
    }

    private Course createCourseEntity() {
        Course course = new Course();
        course.setAuthor(teacher);
        course.setCategory(category);
        course.setCode("COURSE-" + UUID.randomUUID());
        course.setTitle("Course entity");
        courseRepository.save(course);
        return course;
    }
}

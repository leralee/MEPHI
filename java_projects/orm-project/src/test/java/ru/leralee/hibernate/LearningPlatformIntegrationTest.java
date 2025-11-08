package ru.leralee.hibernate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.leralee.hibernate.dto.request.*;
import ru.leralee.hibernate.enums.EnrollmentRole;
import ru.leralee.hibernate.enums.QuestionType;
import ru.leralee.hibernate.enums.StudentStatus;
import ru.leralee.hibernate.enums.SubmissionStatus;
import ru.leralee.hibernate.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author valeriali
 * @project orm-project
 */
class LearningPlatformIntegrationTest extends IntegrationTestBase {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private CourseReviewService courseReviewService;

    @Test
    void fullLearningFlow() {
        TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setFirstName("Alice");
        teacherRequest.setLastName("Tutor");
        teacherRequest.setAcademicTitle("Professor");
        teacherRequest.setBirthDate(LocalDate.of(1980, 1, 1));
        teacherRequest.setExternalRef("T-001");
        teacherRequest.setEmail("alice.tutor@example.com");
        var teacher = userService.createTeacher(teacherRequest);

        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("Bob");
        studentRequest.setLastName("Student");
        studentRequest.setBirthDate(LocalDate.of(2002, 5, 5));
        studentRequest.setExternalRef("S-001");
        studentRequest.setEmail("bob.student@example.com");
        studentRequest.setStudentNo("STU01");
        studentRequest.setStatus(StudentStatus.ACTIVE);
        var student = userService.createStudent(studentRequest);

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("Programming");
        categoryRequest.setSlug("programming");
        var category = categoryService.create(categoryRequest);

        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setCode("CS101");
        courseRequest.setTitle("Hibernate Basics");
        courseRequest.setDescription("ORM introduction");
        courseRequest.setCredits(4);
        courseRequest.setAuthorId(teacher.getId());
        courseRequest.setCategoryId(category.getId());
        var course = courseService.create(courseRequest);

        ModuleRequest moduleRequest = new ModuleRequest();
        moduleRequest.setTitle("Module 1");
        moduleRequest.setDescription("Intro module");
        moduleRequest.setOrderIndex(1);
        moduleRequest.setCourseId(course.getId());
        var module = moduleService.create(moduleRequest);

        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setTitle("Lesson 1");
        lessonRequest.setSyllabus("Hibernate overview");
        lessonRequest.setCourseId(course.getId());
        lessonRequest.setModuleId(module.getId());
        lessonRequest.setResources(List.of("https://example.com/lecture"));
        var lesson = lessonService.create(lessonRequest);

        AssignmentRequest assignmentRequest = new AssignmentRequest();
        assignmentRequest.setTitle("Homework 1");
        assignmentRequest.setDescription("Answer questions");
        assignmentRequest.setLessonId(lesson.getId());
        assignmentRequest.setDueDate(LocalDateTime.now().plusDays(7));
        assignmentRequest.setMaxPoints(100);
        var assignment = assignmentService.create(assignmentRequest);

        var enrollment = enrollmentService.enrollStudent(student.getId(), course.getId(), EnrollmentRole.STUDENT);
        assertThat(enrollment.getId()).isNotNull();

        SubmissionRequest submissionRequest = new SubmissionRequest();
        submissionRequest.setAssignmentId(assignment.getId());
        submissionRequest.setStudentId(student.getId());
        submissionRequest.setContentUrl("https://example.com/submission");
        submissionRequest.setStatus(SubmissionStatus.PENDING);
        submissionRequest.setAttemptNo(1);
        var submission = submissionService.submit(submissionRequest);
        assertThat(submission.getId()).isNotNull();

        QuizRequest quizRequest = new QuizRequest();
        quizRequest.setTitle("Module Quiz");
        quizRequest.setModuleId(module.getId());
        quizRequest.setTimeLimitMinutes(15);
        var quiz = quizService.createQuiz(quizRequest);

        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuizId(quiz.getId());
        questionRequest.setText("What does ORM stand for?");
        questionRequest.setType(QuestionType.SINGLE_CHOICE);
        questionRequest.setOrderIndex(1);
        var question = quizService.addQuestion(questionRequest);

        AnswerOptionRequest optionRequest = new AnswerOptionRequest();
        optionRequest.setQuestionId(question.getId());
        optionRequest.setText("Object Relational Mapping");
        optionRequest.setCorrect(true);
        var answer = quizService.addAnswerOption(optionRequest);
        assertThat(answer.isCorrect()).isTrue();

        QuizSubmissionRequest quizSubmissionRequest = new QuizSubmissionRequest();
        quizSubmissionRequest.setQuizId(quiz.getId());
        quizSubmissionRequest.setStudentId(student.getId());
        quizSubmissionRequest.setScore(100.0);
        quizSubmissionRequest.setPassed(true);
        var quizSubmission = quizService.submitQuiz(quizSubmissionRequest);
        assertThat(quizSubmission.getScore()).isEqualTo(100.0);

        CourseReviewRequest reviewRequest = new CourseReviewRequest();
        reviewRequest.setCourseId(course.getId());
        reviewRequest.setStudentId(student.getId());
        reviewRequest.setRating(5);
        reviewRequest.setComment("Great course");
        var review = courseReviewService.create(reviewRequest);
        assertThat(review.getRating()).isEqualTo(5);

        assertThat(lessonService.findByCourseId(course.getId())).hasSize(1);
        assertThat(assignmentService.findByCourse(course.getId())).hasSize(1);
        assertThat(submissionService.findByAssignment(assignment.getId())).hasSize(1);
        assertThat(quizService.getQuizQuestions(quiz.getId())).hasSize(1);
        assertThat(courseReviewService.getCourseReviews(course.getId())).hasSize(1);
    }
}

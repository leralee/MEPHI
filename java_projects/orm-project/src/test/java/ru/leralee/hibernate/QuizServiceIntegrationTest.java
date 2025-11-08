package ru.leralee.hibernate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.leralee.hibernate.dto.request.AnswerOptionRequest;
import ru.leralee.hibernate.dto.request.QuestionRequest;
import ru.leralee.hibernate.dto.request.QuizRequest;
import ru.leralee.hibernate.dto.request.QuizSubmissionRequest;
import ru.leralee.hibernate.dto.response.AnswerOptionResponse;
import ru.leralee.hibernate.dto.response.QuestionResponse;
import ru.leralee.hibernate.dto.response.QuizResponse;
import ru.leralee.hibernate.dto.response.QuizSubmissionResponse;
import ru.leralee.hibernate.entity.Category;
import ru.leralee.hibernate.entity.Course;
import ru.leralee.hibernate.entity.CourseModule;
import ru.leralee.hibernate.entity.person.Student;
import ru.leralee.hibernate.entity.person.Teacher;
import ru.leralee.hibernate.enums.QuestionType;
import ru.leralee.hibernate.enums.StudentStatus;
import ru.leralee.hibernate.enums.UserRole;
import ru.leralee.hibernate.repository.*;
import ru.leralee.hibernate.service.QuizService;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class QuizServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private QuizService quizService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseModuleRepository moduleRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private CourseModule module;
    private Student student;

    @BeforeEach
    void init() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Quiz");
        teacher.setLastName("Master");
        teacher.setEmail("quiz." + UUID.randomUUID() + "@example.com");
        teacher.setRole(UserRole.TEACHER);
        teacher.setBirthDate(LocalDate.of(1988, 3, 3));
        teacherRepository.save(teacher);

        student = new Student();
        student.setFirstName("Quiz");
        student.setLastName("Taker");
        student.setEmail("student." + UUID.randomUUID() + "@example.com");
        student.setStudentNo("ST" + UUID.randomUUID().toString().substring(0, 5));
        student.setStatus(StudentStatus.ACTIVE);
        student.setRole(UserRole.STUDENT);
        studentRepository.save(student);

        Category category = new Category();
        category.setName("Testing" + UUID.randomUUID());
        category.setSlug("testing" + UUID.randomUUID());
        categoryRepository.save(category);

        Course course = new Course();
        course.setAuthor(teacher);
        course.setCategory(category);
        course.setCode("QUIZ-" + UUID.randomUUID());
        course.setTitle("Quiz Ready");
        courseRepository.save(course);

        module = new CourseModule();
        module.setCourse(course);
        module.setTitle("Quiz Module");
        module.setDescription("Module for quiz tests");
        module.setOrderIndex(1);
        moduleRepository.save(module);
    }

    @Test
    void shouldCreateQuizWithQuestion() {
        QuizResponse quiz = createQuiz();
        QuestionResponse question = addQuestion(quiz.getId());
        AnswerOptionResponse option = addOption(question.getId());

        assertThat(option.isCorrect()).isTrue();
        assertThat(quizService.getQuizQuestions(quiz.getId()))
                .extracting(QuestionResponse::getText)
                .contains("Что такое ORM?");
    }

    @Test
    void shouldStoreQuizSubmission() {
        QuizResponse quiz = createQuiz();
        addQuestion(quiz.getId());
        addOption(quizService.getQuizQuestions(quiz.getId()).get(0).getId());

        QuizSubmissionRequest request = new QuizSubmissionRequest();
        request.setQuizId(quiz.getId());
        request.setStudentId(student.getId());
        request.setScore(95.0);
        request.setPassed(true);

        QuizSubmissionResponse response = quizService.submitQuiz(request);
        assertThat(response.getScore()).isEqualTo(95.0);
        assertThat(quizService.getQuizSubmissions(quiz.getId())).hasSize(1);
        assertThat(quizService.getStudentQuizResults(student.getId())).hasSize(1);
    }

    private QuizResponse createQuiz() {
        QuizRequest quizRequest = new QuizRequest();
        quizRequest.setModuleId(module.getId());
        quizRequest.setTitle("Intro Quiz");
        quizRequest.setTimeLimitMinutes(10);
        return quizService.createQuiz(quizRequest);
    }

    private QuestionResponse addQuestion(UUID quizId) {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuizId(quizId);
        questionRequest.setType(QuestionType.SINGLE_CHOICE);
        questionRequest.setText("Что такое ORM?");
        questionRequest.setOrderIndex(1);
        return quizService.addQuestion(questionRequest);
    }

    private AnswerOptionResponse addOption(UUID questionId) {
        AnswerOptionRequest optionRequest = new AnswerOptionRequest();
        optionRequest.setQuestionId(questionId);
        optionRequest.setText("Object Relational Mapping");
        optionRequest.setCorrect(true);
        return quizService.addAnswerOption(optionRequest);
    }
}

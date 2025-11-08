package ru.leralee.hibernate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leralee.hibernate.dto.request.AnswerOptionRequest;
import ru.leralee.hibernate.dto.request.QuestionRequest;
import ru.leralee.hibernate.dto.request.QuizRequest;
import ru.leralee.hibernate.dto.request.QuizSubmissionRequest;
import ru.leralee.hibernate.dto.response.AnswerOptionResponse;
import ru.leralee.hibernate.dto.response.QuestionResponse;
import ru.leralee.hibernate.dto.response.QuizResponse;
import ru.leralee.hibernate.dto.response.QuizSubmissionResponse;
import ru.leralee.hibernate.entity.*;
import ru.leralee.hibernate.entity.person.Student;
import ru.leralee.hibernate.exception.NotFoundException;
import ru.leralee.hibernate.mapper.AnswerOptionMapper;
import ru.leralee.hibernate.mapper.QuestionMapper;
import ru.leralee.hibernate.mapper.QuizMapper;
import ru.leralee.hibernate.mapper.QuizSubmissionMapper;
import ru.leralee.hibernate.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final CourseModuleRepository moduleRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final StudentRepository studentRepository;
    private final QuizMapper quizMapper;
    private final QuestionMapper questionMapper;
    private final AnswerOptionMapper answerOptionMapper;
    private final QuizSubmissionMapper quizSubmissionMapper;

    @Transactional
    public QuizResponse createQuiz(QuizRequest request) {
        CourseModule module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new NotFoundException("Module not found with id: " + request.getModuleId()));

        Quiz quiz = quizMapper.toEntity(request);
        quiz.setModule(module);

        Quiz saved = quizRepository.save(quiz);
        return quizMapper.toResponse(saved);
    }

    @Transactional
    public QuestionResponse addQuestion(QuestionRequest request) {
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new NotFoundException("Quiz not found with id: " + request.getQuizId()));
        Question question = questionMapper.toEntity(request);
        question.setQuiz(quiz);
        Question saved = questionRepository.save(question);
        return questionMapper.toResponse(saved);
    }

    @Transactional
    public AnswerOptionResponse addAnswerOption(AnswerOptionRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new NotFoundException("Question not found with id: " + request.getQuestionId()));
        AnswerOption option = answerOptionMapper.toEntity(request);
        option.setQuestion(question);
        AnswerOption saved = answerOptionRepository.save(option);
        return answerOptionMapper.toResponse(saved);
    }

    @Transactional
    public QuizSubmissionResponse submitQuiz(QuizSubmissionRequest request) {
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new NotFoundException("Quiz not found with id: " + request.getQuizId()));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + request.getStudentId()));

        QuizSubmission submission = quizSubmissionMapper.toEntity(request);
        submission.setQuiz(quiz);
        submission.setStudent(student);
        submission.setTakenAt(LocalDateTime.now());

        QuizSubmission saved = quizSubmissionRepository.save(submission);
        return quizSubmissionMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public QuizResponse findQuiz(UUID id) {
        return quizMapper.toResponse(getQuiz(id));
    }

    @Transactional(readOnly = true)
    public List<QuizSubmissionResponse> getQuizSubmissions(UUID quizId) {
        return quizSubmissionMapper.toResponseList(quizSubmissionRepository.findByQuizId(quizId));
    }

    @Transactional(readOnly = true)
    public List<QuizSubmissionResponse> getStudentQuizResults(UUID studentId) {
        return quizSubmissionMapper.toResponseList(quizSubmissionRepository.findByStudentId(studentId));
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuizQuestions(UUID quizId) {
        return questionMapper.toResponseList(questionRepository.findByQuizIdOrderByOrderIndexAsc(quizId));
    }

    private Quiz getQuiz(UUID id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quiz not found with id: " + id));
    }
}

package ru.leralee.hibernate.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.leralee.hibernate.dto.request.AnswerOptionRequest;
import ru.leralee.hibernate.dto.request.QuestionRequest;
import ru.leralee.hibernate.dto.request.QuizRequest;
import ru.leralee.hibernate.dto.request.QuizSubmissionRequest;
import ru.leralee.hibernate.dto.response.AnswerOptionResponse;
import ru.leralee.hibernate.dto.response.QuestionResponse;
import ru.leralee.hibernate.dto.response.QuizResponse;
import ru.leralee.hibernate.dto.response.QuizSubmissionResponse;
import ru.leralee.hibernate.service.QuizService;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@RestController
@RequestMapping("/api/quizzes")
@Validated
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping
    public QuizResponse createQuiz(@Valid @RequestBody QuizRequest request) {
        return quizService.createQuiz(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping("/{quizId}/questions")
    public QuestionResponse addQuestion(@PathVariable UUID quizId, @Valid @RequestBody QuestionRequest request) {
        request.setQuizId(quizId);
        return quizService.addQuestion(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping("/questions/{questionId}/options")
    public AnswerOptionResponse addOption(@PathVariable UUID questionId, @Valid @RequestBody AnswerOptionRequest request) {
        request.setQuestionId(questionId);
        return quizService.addAnswerOption(request);
    }

    @GetMapping("/{quizId}")
    public QuizResponse findQuiz(@PathVariable UUID quizId) {
        return quizService.findQuiz(quizId);
    }

    @GetMapping("/{quizId}/questions")
    public List<QuestionResponse> getQuestions(@PathVariable UUID quizId) {
        return quizService.getQuizQuestions(quizId);
    }

    @PostMapping("/{quizId}/submit")
    public QuizSubmissionResponse submit(@PathVariable UUID quizId, @Valid @RequestBody QuizSubmissionRequest request) {
        request.setQuizId(quizId);
        return quizService.submitQuiz(request);
    }

    @GetMapping("/{quizId}/submissions")
    public List<QuizSubmissionResponse> quizSubmissions(@PathVariable UUID quizId) {
        return quizService.getQuizSubmissions(quizId);
    }

    @GetMapping("/students/{studentId}/submissions")
    public List<QuizSubmissionResponse> studentResults(@PathVariable UUID studentId) {
        return quizService.getStudentQuizResults(studentId);
    }
}

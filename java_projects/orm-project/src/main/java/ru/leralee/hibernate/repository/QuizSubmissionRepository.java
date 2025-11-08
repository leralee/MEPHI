package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.QuizSubmission;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, UUID> {

    List<QuizSubmission> findByQuizId(UUID quizId);

    List<QuizSubmission> findByStudentId(UUID studentId);
}

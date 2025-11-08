package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.Quiz;

import java.util.Optional;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface QuizRepository extends JpaRepository<Quiz, UUID> {

    Optional<Quiz> findByModuleId(UUID moduleId);
}

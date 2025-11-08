package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.Assignment;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

    List<Assignment> findByLessonId(UUID lessonId);

    List<Assignment> findByLesson_Module_Course_Id(UUID courseId);
}

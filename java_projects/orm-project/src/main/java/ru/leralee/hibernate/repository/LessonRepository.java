package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.Lesson;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    List<Lesson> findByModuleId(UUID moduleId);

    List<Lesson> findByModule_Course_Id(UUID courseId);
}

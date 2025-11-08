package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.CourseModule;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface CourseModuleRepository extends JpaRepository<CourseModule, UUID> {

    List<CourseModule> findByCourseIdOrderByOrderIndexAsc(UUID courseId);
}

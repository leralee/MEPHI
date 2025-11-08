package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.Course;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface CourseRepository extends JpaRepository<Course, UUID> {

    Optional<Course> findByCode(String code);

    @EntityGraph(attributePaths = {"author", "category"})
    List<Course> findAllByCategory_Name(String categoryName);
}

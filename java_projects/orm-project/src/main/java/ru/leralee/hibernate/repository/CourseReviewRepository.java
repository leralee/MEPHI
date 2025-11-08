package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.CourseReview;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface CourseReviewRepository extends JpaRepository<CourseReview, UUID> {

    List<CourseReview> findByCourseId(UUID courseId);
}

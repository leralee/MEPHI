package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.Enrollment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    List<Enrollment> findByStudentId(UUID studentId);

    List<Enrollment> findByCourseId(UUID courseId);

    Optional<Enrollment> findByStudentIdAndCourseId(UUID studentId, UUID courseId);
}

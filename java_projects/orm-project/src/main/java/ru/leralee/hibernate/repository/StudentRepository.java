package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.person.Student;
import ru.leralee.hibernate.enums.StudentStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface StudentRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findByStudentNo(String studentNo);

    List<Student> findByStatus(StudentStatus status);

    @EntityGraph(attributePaths = {"emails", "phones"})
    List<Student> findDistinctByStatus(StudentStatus status);
}

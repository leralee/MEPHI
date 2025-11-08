package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.person.Teacher;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    List<Teacher> findByAcademicTitle(String academicTitle);
}

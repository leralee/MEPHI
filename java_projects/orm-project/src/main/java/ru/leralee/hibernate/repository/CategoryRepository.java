package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.Category;

import java.util.Optional;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findBySlug(String slug);
}

package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.Tag;

import java.util.Optional;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface TagRepository extends JpaRepository<Tag, UUID> {

    Optional<Tag> findBySlug(String slug);
}

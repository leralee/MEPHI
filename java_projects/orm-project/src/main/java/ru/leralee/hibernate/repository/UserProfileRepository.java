package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.person.UserProfile;

import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
}

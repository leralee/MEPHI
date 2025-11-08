package ru.leralee.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leralee.hibernate.entity.person.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    List<User> findByLastName(String lastName);
}

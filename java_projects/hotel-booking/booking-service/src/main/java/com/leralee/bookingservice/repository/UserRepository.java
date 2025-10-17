package com.leralee.bookingservice.repository;

import com.leralee.bookingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author valeriali
 * @project hotel-booking
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndPassword(String username, String password);
}

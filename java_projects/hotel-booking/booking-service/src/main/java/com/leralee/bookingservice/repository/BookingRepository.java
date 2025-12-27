package com.leralee.bookingservice.repository;

import com.leralee.bookingservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author valeriali
 * @project hotel-booking
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByUserUsername(String username, Pageable pageable);
    Optional<Booking> findByIdAndUserUsername(Long id, String username);
    Optional<Booking> findByIdempotencyKey(String idempotencyKey);
}

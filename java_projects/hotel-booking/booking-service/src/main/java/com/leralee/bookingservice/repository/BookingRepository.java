package com.leralee.bookingservice.repository;

import com.leralee.bookingservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author valeriali
 * @project hotel-booking
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUserId(Long userId);

    @Query("select b.user.id from Booking b where b.user.username = :username")
    Long findUserIdByUsername(@Param("username") String username);
}

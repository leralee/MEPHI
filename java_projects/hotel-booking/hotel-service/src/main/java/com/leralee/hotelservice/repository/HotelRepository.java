package com.leralee.hotelservice.repository;

import com.leralee.hotelservice.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author valeriali
 * @project hotel-booking
 */
public interface HotelRepository extends JpaRepository<Hotel, Long> {
}

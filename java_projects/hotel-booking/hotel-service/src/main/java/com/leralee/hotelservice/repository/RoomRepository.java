package com.leralee.hotelservice.repository;

import com.leralee.hotelservice.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author valeriali
 * @project hotel-booking
 */
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByAvailableTrueOrderByTimesBookedAscIdAsc();
}

package com.leralee.hotelservice.service;

import com.leralee.hotelservice.dto.RoomDto;
import com.leralee.hotelservice.entity.Room;
import com.leralee.hotelservice.mapper.RoomMapper;
import com.leralee.hotelservice.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public List<RoomDto> getAllRooms() {
        return roomRepository.findAll().stream().map(roomMapper::toDto).toList();
    }

    public RoomDto createRoom(RoomDto dto) {
        Room room = roomMapper.toEntity(dto);
        Room saved = roomRepository.save(room);
        return roomMapper.toDto(saved);
    }

    public List<RoomDto> getRecommendedRooms() {
        return roomRepository.findByAvailableTrueOrderByTimesBookedAscIdAsc()
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }

    @Transactional
    public boolean confirmAvailability(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        if (!room.isAvailable()) return false;

        room.setAvailable(false);
        room.setTimesBooked(room.getTimesBooked() + 1);
        roomRepository.save(room);
        return true;
    }

    @Transactional
    public void releaseRoom(Long id) {
        roomRepository.findById(id).ifPresent(room -> {
            room.setAvailable(true);
            roomRepository.save(room);
        });
    }

    public List<RoomDto> getRoomsSortedByBookings() {
        return roomRepository.findAll(Sort.by(Sort.Direction.DESC, "timesBooked"))
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }

    public RoomDto updateRoom(Long id, RoomDto dto) {
        Room existing = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        existing.setNumber(dto.getNumber());
        existing.setAvailable(dto.isAvailable());
        existing.setTimesBooked(dto.getTimesBooked());

        return roomMapper.toDto(roomRepository.save(existing));
    }

    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new IllegalArgumentException("Room not found");
        }
        roomRepository.deleteById(id);
    }
}

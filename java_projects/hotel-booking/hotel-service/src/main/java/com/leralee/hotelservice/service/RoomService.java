package com.leralee.hotelservice.service;

import com.leralee.hotelservice.dto.OccupancyStatsDto;
import com.leralee.hotelservice.dto.RoomDto;
import com.leralee.hotelservice.entity.Room;
import com.leralee.hotelservice.mapper.RoomMapper;
import com.leralee.hotelservice.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

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

    public List<RoomDto> searchRooms(Boolean available, Integer minTimesBooked, String sortBy, Sort.Direction direction) {
        Sort sort = Sort.by(direction == null ? Sort.Direction.ASC : direction, sortBy == null ? "id" : sortBy);
        List<Room> rooms = roomRepository.findAll(sort);

        if (available != null) {
            rooms = rooms.stream()
                    .filter(r -> r.isAvailable() == available)
                    .toList();
        }
        if (minTimesBooked != null) {
            rooms = rooms.stream()
                    .filter(r -> r.getTimesBooked() >= minTimesBooked)
                    .toList();
        }
        return rooms.stream().map(roomMapper::toDto).toList();
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
    public boolean confirmAvailability(Long id, String idempotencyKey) {
        Room room = roomRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        if (idempotencyKey != null && Objects.equals(idempotencyKey, room.getLastHoldToken())) {
            return true;
        }
        if (!room.isAvailable()) return false;

        room.setAvailable(false);
        room.setTimesBooked(room.getTimesBooked() + 1);
        room.setLastHoldToken(idempotencyKey);
        roomRepository.save(room);
        return true;
    }

    @Transactional
    public void releaseRoom(Long id) {
        roomRepository.findByIdForUpdate(id).ifPresent(room -> {
            room.setAvailable(true);
            room.setLastHoldToken(null);
            roomRepository.save(room);
        });
    }

    @Transactional
    public RoomDto allocateRoom(Long hotelId) {
        Room room = roomRepository.findFirstByHotelIdAndAvailableTrueOrderByTimesBookedAscIdAsc(hotelId)
                .orElseThrow(() -> new NoSuchElementException("No available rooms in hotel " + hotelId));

        room.setAvailable(false);
        room.setTimesBooked(room.getTimesBooked() + 1);

        Room saved = roomRepository.save(room);
        return roomMapper.toDto(saved);
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

    public List<RoomDto> getRoomsSortedByBookings() {
        return roomRepository.findAll(Sort.by(Sort.Direction.DESC, "timesBooked"))
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }

    public OccupancyStatsDto getHotelStats(Long hotelId) {
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        if (rooms.isEmpty()) {
            throw new NoSuchElementException("No rooms found for hotel " + hotelId);
        }
        return toStats(rooms);
    }

    public OccupancyStatsDto getGlobalStats() {
        return toStats(roomRepository.findAll());
    }

    private OccupancyStatsDto toStats(List<Room> rooms) {
        long total = rooms.size();
        long available = rooms.stream().filter(Room::isAvailable).count();
        long occupied = total - available;
        double avgTimes = rooms.stream().mapToInt(Room::getTimesBooked).average().orElse(0d);

        return OccupancyStatsDto.builder()
                .totalRooms(total)
                .availableRooms(available)
                .occupiedRooms(occupied)
                .averageTimesBooked(avgTimes)
                .build();
    }
}

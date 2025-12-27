package com.leralee.hotelservice.controller;

import com.leralee.hotelservice.dto.RoomDto;
import com.leralee.hotelservice.dto.OccupancyStatsDto;
import com.leralee.hotelservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author valeriali
 * @project hotel-booking
 */
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public List<RoomDto> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/search")
    public List<RoomDto> searchRooms(@RequestParam(required = false) Boolean available,
                                     @RequestParam(required = false) Integer minTimesBooked,
                                     @RequestParam(required = false) String sortBy,
                                     @RequestParam(required = false) Sort.Direction direction) {
        return roomService.searchRooms(available, minTimesBooked, sortBy, direction);
    }

    @GetMapping("/recommend")
    public List<RoomDto> getRecommendedRooms() {
        return roomService.getRecommendedRooms();
    }

    @PostMapping("/allocate")
    public RoomDto allocateRoom(@RequestParam Long hotelId) {
        return roomService.allocateRoom(hotelId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomDto createRoom(@RequestBody RoomDto roomDto) {
        return roomService.createRoom(roomDto);
    }

    @PostMapping("/{id}/confirm-availability")
    @ResponseStatus(HttpStatus.OK)
    public boolean confirmAvailability(@PathVariable Long id,
                                       @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        return roomService.confirmAvailability(id, idempotencyKey);
    }

    @PostMapping("/{id}/release")
    @ResponseStatus(HttpStatus.OK)
    public void releaseRoom(@PathVariable Long id) {
        roomService.releaseRoom(id);
    }

    @PutMapping("/{id}")
    public RoomDto updateRoom(@PathVariable Long id, @RequestBody RoomDto dto) {
        return roomService.updateRoom(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }

    @GetMapping("/stats")
    public List<RoomDto> getRoomStats() {
        return roomService.getRoomsSortedByBookings();
    }

    @GetMapping("/analytics/occupancy")
    public OccupancyStatsDto getGlobalOccupancy() {
        return roomService.getGlobalStats();
    }
}

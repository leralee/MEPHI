package com.leralee.bookingservice.controller;

import com.leralee.bookingservice.dto.BookingDto;
import com.leralee.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author valeriali
 * @project hotel-booking
 */
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> getAll() {
        return bookingService.getAllBookings();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestBody BookingDto dto) {
        return bookingService.createBooking(dto);
    }

    @PutMapping("/{id}/confirm")
    public BookingDto confirm(@PathVariable Long id) {
        return bookingService.confirmBooking(id);
    }

    @PutMapping("/{id}/cancel")
    public BookingDto cancel(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }
}

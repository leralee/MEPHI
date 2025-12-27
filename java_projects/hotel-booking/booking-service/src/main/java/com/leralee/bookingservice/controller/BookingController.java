package com.leralee.bookingservice.controller;

import com.leralee.bookingservice.dto.BookingDto;
import com.leralee.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;

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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Page<BookingDto> getAll(Pageable pageable) {
        return bookingService.getAllBookings(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BookingDto create(@Valid @RequestBody BookingDto dto,
                             @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        return bookingService.createBooking(dto, idempotencyKey);
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BookingDto confirm(@PathVariable Long id) {
        return bookingService.confirmBooking(id);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BookingDto cancel(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }
}

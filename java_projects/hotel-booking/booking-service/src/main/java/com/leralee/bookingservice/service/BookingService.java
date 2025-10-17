package com.leralee.bookingservice.service;

import com.leralee.bookingservice.client.HotelClient;
import com.leralee.bookingservice.dto.BookingDto;
import com.leralee.bookingservice.entity.Booking;
import com.leralee.bookingservice.entity.BookingStatus;
import com.leralee.bookingservice.mapper.BookingMapper;
import com.leralee.bookingservice.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final HotelClient hotelClient;

    public List<BookingDto> getAllBookings() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Long userId = bookingRepository.findUserIdByUsername(username);

        return bookingRepository.findAllByUserId(userId)
                .stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Transactional
    public BookingDto createBooking(BookingDto dto) {
        Booking booking = bookingMapper.toEntity(dto);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        boolean confirmed = hotelClient.confirmAvailability(dto.getRoomId());
        if (confirmed) {
            booking.setStatus(BookingStatus.CONFIRMED);
        } else {
            booking.setStatus(BookingStatus.CANCELLED);
            hotelClient.releaseRoom(dto.getRoomId());
        }
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    public BookingDto confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    public BookingDto cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }
}

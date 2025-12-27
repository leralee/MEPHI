package com.leralee.bookingservice.service;

import com.leralee.bookingservice.dto.BookingDto;
import com.leralee.bookingservice.dto.RoomDto;
import com.leralee.bookingservice.entity.Booking;
import com.leralee.bookingservice.entity.BookingStatus;
import com.leralee.bookingservice.entity.User;
import com.leralee.bookingservice.mapper.BookingMapper;
import com.leralee.bookingservice.repository.BookingRepository;
import com.leralee.bookingservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.lang.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final HotelResilientClient hotelClient;
    private final UserRepository userRepository;

    public Page<BookingDto> getAllBookings(Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return bookingRepository.findByUserUsername(username, pageable)
                .map(bookingMapper::toDto);
    }

    @Transactional(noRollbackFor = BookingAllocationException.class)
    public BookingDto createBooking(BookingDto dto, @Nullable String idempotencyHeader) {
        validateDates(dto);

        String effectiveKey = resolveIdempotencyKey(dto.getIdempotencyKey(), idempotencyHeader);
        if (effectiveKey != null) {
            Optional<Booking> existing = bookingRepository.findByIdempotencyKey(effectiveKey);
            if (existing.isPresent()) {
                return bookingMapper.toDto(existing.get());
            }
        }

        Booking booking = bookingMapper.toEntity(dto);
        booking.setHotelId(dto.getHotelId());
        booking.setIdempotencyKey(effectiveKey != null ? effectiveKey : UUID.randomUUID().toString());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        attachCurrentUserOrFail(booking);
        booking = bookingRepository.save(booking);

        RoomDto allocatedRoom;
        try {
            allocatedRoom = hotelClient.allocateRoom(dto.getHotelId());
        } catch (BookingAllocationException ex) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            throw ex;
        }

        booking.setRoomId(allocatedRoom.getId());
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());

        try {
            booking = bookingRepository.save(booking);
        } catch (RuntimeException ex) {
            try {
                hotelClient.releaseRoom(booking.getRoomId());
            } catch (Exception ignored) {
                log.warn("Failed to release room {} after DB error: {}", booking.getRoomId(), ignored.getMessage());
            }
            throw ex;
        }

        return bookingMapper.toDto(booking);
    }

    public BookingDto confirmBooking(Long id) {
        Booking booking = loadForCurrentUser(id);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    public BookingDto cancelBooking(Long id) {
        Booking booking = loadForCurrentUser(id);
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    private String resolveIdempotencyKey(String bodyKey, String headerKey) {
        return headerKey != null && !headerKey.isBlank() ? headerKey : bodyKey;
    }

    private void attachCurrentUserOrFail(Booking booking) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated");
        }
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        booking.setUser(user);
    }

    private Booking loadForCurrentUser(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return bookingRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        }
        return bookingRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new RuntimeException("Booking not found or access denied"));
    }

    private void validateDates(BookingDto dto) {
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new IllegalArgumentException("startDate and endDate are required");
        }
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("endDate must be after or equal to startDate");
        }
    }
}

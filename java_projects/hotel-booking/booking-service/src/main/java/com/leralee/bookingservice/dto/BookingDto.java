package com.leralee.bookingservice.dto;

import com.leralee.bookingservice.entity.BookingStatus;
import lombok.*;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    private Long userId;
    private Long roomId;
    @NotNull
    private Long hotelId;
    @Size(max = 64)
    private String idempotencyKey;
    @NotNull
    @FutureOrPresent
    private LocalDate startDate;
    @NotNull
    @FutureOrPresent
    private LocalDate endDate;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
}

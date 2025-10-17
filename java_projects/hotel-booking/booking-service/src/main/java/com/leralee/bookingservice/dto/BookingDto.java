package com.leralee.bookingservice.dto;

import com.leralee.bookingservice.entity.BookingStatus;
import lombok.*;

import java.time.LocalDateTime;

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
    private BookingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
}

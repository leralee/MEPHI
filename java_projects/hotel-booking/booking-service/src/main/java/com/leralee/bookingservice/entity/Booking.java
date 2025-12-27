package com.leralee.bookingservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long roomId; // id комнаты из hotel-service
    private Long hotelId;

    @Column(name = "idempotency_key", unique = true, length = 64)
    private String idempotencyKey;

    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;

    @Version
    private Long version;
}

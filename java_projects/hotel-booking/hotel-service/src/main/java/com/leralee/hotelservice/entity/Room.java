package com.leralee.hotelservice.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author valeriali
 * @project hotel-booking
 */

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    private String number;         // номер комнаты
    private boolean available;     // доступен ли (например, не на ремонте)
    private int timesBooked = 0;       // сколько раз бронировали
    private String lastHoldToken;  // идемпотентность подтверждения доступности

    @Version
    private Long version;
}

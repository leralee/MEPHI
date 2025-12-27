package com.leralee.bookingservice.dto;

import lombok.Data;

/**
 * Lightweight room payload returned by hotel-service allocation endpoint.
 */
@Data
public class RoomDto {
    private Long id;
    private Long hotelId;
    private String number;
    private boolean available;
    private int timesBooked;
}

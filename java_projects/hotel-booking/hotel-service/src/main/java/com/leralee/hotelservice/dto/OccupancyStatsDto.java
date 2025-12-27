package com.leralee.hotelservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OccupancyStatsDto {
    private long totalRooms;
    private long availableRooms;
    private long occupiedRooms;
    private double averageTimesBooked;
}

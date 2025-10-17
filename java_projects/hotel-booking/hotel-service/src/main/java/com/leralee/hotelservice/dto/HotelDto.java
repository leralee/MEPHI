package com.leralee.hotelservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDto {
    private Long id;
    private String name;
    private String address;
}

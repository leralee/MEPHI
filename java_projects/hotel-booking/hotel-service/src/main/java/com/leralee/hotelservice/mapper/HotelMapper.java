package com.leralee.hotelservice.mapper;


import com.leralee.hotelservice.dto.HotelDto;
import com.leralee.hotelservice.entity.Hotel;
import org.mapstruct.Mapper;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Mapper(componentModel = "spring")
public interface HotelMapper {
    HotelDto toDto(Hotel entity);
    Hotel toEntity(HotelDto dto);
}

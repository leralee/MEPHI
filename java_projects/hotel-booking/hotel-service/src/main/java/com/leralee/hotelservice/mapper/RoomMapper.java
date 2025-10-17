package com.leralee.hotelservice.mapper;

import com.leralee.hotelservice.dto.RoomDto;
import com.leralee.hotelservice.entity.Room;
import org.mapstruct.Mapper;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Mapper(componentModel = "spring", uses = HotelMapper.class)
public interface RoomMapper {

    RoomDto toDto(Room entity);

    Room toEntity(RoomDto dto);
}

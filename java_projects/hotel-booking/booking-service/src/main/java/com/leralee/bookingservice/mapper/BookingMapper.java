package com.leralee.bookingservice.mapper;

import com.leralee.bookingservice.dto.BookingDto;
import com.leralee.bookingservice.entity.Booking;
import com.leralee.bookingservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author valeriali
 * @project hotel-booking
 */
@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "userId", source = "user.id")
    BookingDto toDto(Booking booking);

    @Mapping(target = "user", expression = "java(userFromId(dto.getUserId()))")
    Booking toEntity(BookingDto dto);

    default User userFromId(Long id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }
}

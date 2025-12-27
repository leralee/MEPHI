package com.leralee.bookingservice.client;

import com.leralee.bookingservice.dto.RoomDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
  *
  * @project hotel-booking
  * @author valeriali
 */
@FeignClient(name = "hotel-service")
public interface HotelClient {
    @PostMapping("/api/rooms/{roomId}/confirm-availability")
    boolean confirmAvailability(@PathVariable("roomId") Long roomId);

    @PostMapping("/api/rooms/{roomId}/release")
    void releaseRoom(@PathVariable("roomId") Long roomId);

    @PostMapping("/api/rooms/allocate")
    RoomDto allocateRoom(@RequestParam("hotelId") Long hotelId);
}

package com.leralee.bookingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
}

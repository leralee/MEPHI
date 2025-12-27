package com.leralee.bookingservice.service;

import com.leralee.bookingservice.client.HotelClient;
import com.leralee.bookingservice.dto.RoomDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Wraps HotelClient with resilience patterns: retry + circuit breaker + fallbacks.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HotelResilientClient {

    private final HotelClient hotelClient;
    private static final String HOTEL_CB = "hotelService";

    @Retry(name = HOTEL_CB)
    @CircuitBreaker(name = HOTEL_CB, fallbackMethod = "allocationFallback")
    public RoomDto allocateRoom(Long hotelId) {
        return hotelClient.allocateRoom(hotelId);
    }

    @Retry(name = HOTEL_CB)
    @CircuitBreaker(name = HOTEL_CB, fallbackMethod = "releaseFallback")
    public void releaseRoom(Long roomId) {
        hotelClient.releaseRoom(roomId);
    }

    private RoomDto allocationFallback(Long hotelId, Throwable ex) {
        throw new BookingAllocationException("Failed to allocate room for hotel " + hotelId, ex);
    }

    private void releaseFallback(Long roomId, Throwable ex) {
        log.warn("Failed to release room {} after allocation failure: {}", roomId, ex.getMessage());
    }
}

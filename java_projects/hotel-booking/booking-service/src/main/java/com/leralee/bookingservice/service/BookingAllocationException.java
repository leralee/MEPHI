package com.leralee.bookingservice.service;

/**
 * Signals that a room could not be allocated in hotel-service. We don't rollback
 * the local transaction to keep idempotency records intact.
 */
public class BookingAllocationException extends RuntimeException {
    public BookingAllocationException(String message, Throwable cause) {
        super(message, cause);
    }
}

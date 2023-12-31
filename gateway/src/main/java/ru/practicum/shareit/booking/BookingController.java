package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.WorkingBookingDto;
import ru.practicum.shareit.validation.CreateConstraint;

import static ru.practicum.shareit.utility.HttpHeader.X_SHARER_USER_ID;

@Slf4j
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient bookingClient;
    private static final String DEFAULT_FROM_VALUE = "0";
    private static final String DEFAULT_SIZE_VALUE = "20";
    private static final String DEFAULT_STATE_VALUE = "ALL";

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestBody @Validated(CreateConstraint.class) WorkingBookingDto dto,
                                             @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Creating booking {}, userId={}", dto, userId);
        return bookingClient.addBooking(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                @PathVariable Long bookingId,
                                                @RequestParam(value = "approved", required = false) boolean approved) {
        log.info("Path booking {}, approved={}. userId={}", bookingId, approved, userId);
        return bookingClient.updateBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@PathVariable Long bookingId,
                                           @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.findById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookings(@RequestParam(defaultValue = DEFAULT_STATE_VALUE) String state,
                                                  @RequestHeader(X_SHARER_USER_ID) Long userId,
                                                  @RequestParam(defaultValue = DEFAULT_FROM_VALUE) int from,
                                                  @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) int size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.findAllByBooker(state, userId, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllByItemOwner(@RequestParam(defaultValue = DEFAULT_STATE_VALUE) String state,
                                                     @RequestHeader(X_SHARER_USER_ID) Long userId,
                                                     @RequestParam(defaultValue = DEFAULT_FROM_VALUE) int from,
                                                     @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) int size) {
        log.info("Get bookings owner with state{}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.findAllByItemOwner(state, userId, from, size);
    }
}

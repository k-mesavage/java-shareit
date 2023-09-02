package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.AddingBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validation.CreateConstraint;

import java.util.List;

import static ru.practicum.shareit.utility.HttpHeader.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    BookingService service;

    @PostMapping
    public BookingDto addBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                              @RequestBody @Validated(CreateConstraint.class) AddingBookingDto addingBookingDto) {
        BookingDto newBooking = service.addBooking(userId, addingBookingDto);
        return newBooking;
    }

    @PatchMapping("{bookingId}")
    public BookingDto approveBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                     @PathVariable Long bookingId,
                                     @PathVariable Boolean approve) {
        BookingDto booking = service.requestBooking(approve, bookingId, userId);
        return booking;
    }

    @GetMapping()
    public List<Booking> getAllBookingsFromUser(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                @RequestParam(defaultValue = "ALL", required = false) String state) {
        return service.getAllFromUser(userId, state);
    }
}

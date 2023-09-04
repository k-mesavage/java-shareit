package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
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
                                 @RequestBody
                                 @Validated(CreateConstraint.class) ShortBookingDto shortBookingDto) {
        BookingDto newBooking = service.addBooking(userId, shortBookingDto);
        return newBooking;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam boolean approved) {
        BookingDto booking = service.requestBooking(approved, bookingId, userId);
        return booking;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                     @PathVariable Long bookingId) {
        BookingDto booking = service.getBookingById(bookingId, userId);
        return booking;
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllItemsBookingByOwner(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        return service.getAllItemsBookingByOwner(userId, state);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUser(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                           @RequestParam(defaultValue = "ALL") String state) {
        return service.getAllBookingsByUser(userId, state);
    }
}

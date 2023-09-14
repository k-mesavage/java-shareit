package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.WorkingBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validation.CreateConstraint;

import java.util.List;

import static ru.practicum.shareit.utility.HttpHeader.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping
    public BookingDto addBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                 @RequestBody
                                 @Validated(CreateConstraint.class) WorkingBookingDto workingBookingDto) {
        log.info("Начало обработки запроса на добавление бронирования");
        BookingDto newBooking = service.addBooking(userId, workingBookingDto);
        log.info("Окончание обработки запроса на добавление бронирования");
        return newBooking;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam boolean approved) {
        log.info("Начало обработки запроса на подтверждение бронирования");
        BookingDto booking = service.requestBooking(approved, bookingId, userId);
        log.info("Окончание обработки запроса на подтверждение бронирования");
        return booking;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                     @PathVariable Long bookingId) {
        log.info("Начало обработки запроса на получение информации о бронировании");
        BookingDto booking = service.getBookingById(bookingId, userId);
        log.info("Окончание обработки запроса на получение информации о бронировании");
        return booking;
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllItemsBookingByOwner(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        log.info("Начало обработки запроса на получение списка бронирований владельцем");
        List<BookingDto> bookings = service.getAllItemsBookingByOwner(userId, state, from, size);
        log.info("Окончание обработки запроса на получение списка бронирований владельцем");
        return bookings;
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUser(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                           @RequestParam(defaultValue = "ALL") String state) {
        log.info("Начало обработки запроса на получение списка бронирований");
        List<BookingDto> bookings = service.getAllBookingsByUser(userId, state);
        log.info("Окончание обработки запроса на получение списка бронирований");
        return bookings;
    }
}

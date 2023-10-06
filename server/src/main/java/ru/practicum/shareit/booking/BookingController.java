package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.WorkingBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.params.UserType;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.utility.HttpHeader.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingService service;
    private static final String DEFAULT_FROM_VALUE = "0";
    private static final String DEFAULT_SIZE_VALUE = "20";
    private static final String DEFAULT_STATE_VALUE = "ALL";

    @PostMapping
    public BookingDto addBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                 @RequestBody WorkingBookingDto workingBookingDto) {
        log.info("Начало обработки запроса на добавление бронирования");
        BookingDto newBooking = service.addBooking(userId, workingBookingDto);
        log.info("Окончание обработки запроса на добавление бронирования");
        return newBooking;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                    @PathVariable Long bookingId,
                                    @RequestParam(value = "approved") Boolean approved) {
        log.info("Начало обработки запроса на подтверждение бронирования");
        BookingDto booking = service.updateBooking(approved, bookingId, userId);
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
                                                      @RequestParam(defaultValue = DEFAULT_STATE_VALUE) String state,
                                                      @RequestParam(defaultValue = DEFAULT_FROM_VALUE) int from,
                                                      @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) int size) {
        log.info("Начало обработки запроса на получение списка бронирований владельцем");
        List<BookingDto> bookings = service.getAllBookingsByUser(UserType.OWNER, userId, state, from, size);
        log.info("Окончание обработки запроса на получение списка бронирований владельцем");
        return bookings;
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUser(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                 @RequestParam(defaultValue = DEFAULT_STATE_VALUE) String state,
                                                 @RequestParam(defaultValue = DEFAULT_FROM_VALUE) int from,
                                                 @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) int size) {
        log.info("Начало обработки запроса на получение списка бронирований");
        List<BookingDto> bookings = service.getAllBookingsByUser(UserType.USER, userId, state, from, size);
        log.info("Окончание обработки запроса на получение списка бронирований");
        return bookings;
    }
}

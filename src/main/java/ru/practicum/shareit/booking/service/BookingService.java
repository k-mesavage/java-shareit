package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.WorkingBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(Long bookerId, WorkingBookingDto workingBookingDto);

    BookingDto requestBooking(Boolean approved, Long bookingId, Long userId);

    List<BookingDto> getAllItemsBookingByOwner(Long userId, String state);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getAllBookingsByUser(Long userId, String state);
}

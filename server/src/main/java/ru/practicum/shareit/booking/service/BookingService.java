package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.WorkingBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.params.UserType;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(Long bookerId, WorkingBookingDto workingBookingDto);

    BookingDto updateBooking(Boolean approved, Long bookingId, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getAllBookingsByUser(UserType userType, Long userId, String state, int from, int size);
}

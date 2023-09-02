package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.AddingBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(Long bookerId, AddingBookingDto addingBookingDto);

    BookingDto requestBooking(Boolean approved, Long bookingId, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<Booking> getAllBookings(Long userId, String status);

    List<Booking> getAllFromUser(Long userId, String status);
}

package ru.practicum.shareit.handler;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.params.BookingState;
import ru.practicum.shareit.booking.params.UserType;
import ru.practicum.shareit.booking.storage.BookingStorage;

import static ru.practicum.shareit.booking.params.UserType.USER;
import static ru.practicum.shareit.booking.params.UserType.OWNER;

import java.util.ArrayList;
import java.util.List;

public class GetAll extends BookingStateHandler {

    @Override
    public BookingState getState() {
        return BookingState.ALL;
    }

    @Override
    public List<BookingDto> getBookings(Long senderId, UserType userType) {
        List<Booking> bookings = new ArrayList<>();
        if (userType.equals(USER)) {
            bookings = bookingStorage.findAllByBookerIdOrderByStartDesc(senderId);
        }
        if (userType.equals(OWNER)) {
            bookings = bookingStorage.findAllByItemOwnerIdOrderByStartDesc(senderId);
        }
        return bookingMapper.fromListToDtoList(bookings);
    }

    public GetAll(BookingStorage bookingStorage, BookingMapper bookingMapper) {
        super(bookingStorage, bookingMapper);
    }
}

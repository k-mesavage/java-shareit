package ru.practicum.shareit.handler;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.params.BookingState;
import ru.practicum.shareit.booking.params.UserType;
import ru.practicum.shareit.booking.storage.BookingStorage;

import java.util.ArrayList;
import java.util.List;

public class GetWaiting extends BookingStateHandler {

    @Override
    public BookingState getState() {
        return BookingState.WAITING;
    }

    @Override
    public List<BookingDto> getBookings(Long senderId, UserType userType) {
        List<Booking> bookings = new ArrayList<>();
        if (userType.equals(UserType.USER)) {
            bookings = bookingStorage.findAllByBookerIdAndStatus(senderId, getState().toString());
        }
        if (userType.equals(UserType.OWNER)) {
            bookings = bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(senderId, getState().toString());
        }
        return bookingMapper.fromListToDtoList(bookings);
    }

    public GetWaiting(BookingStorage bookingStorage, BookingMapper bookingMapper) {
        super(bookingStorage, bookingMapper);
    }
}

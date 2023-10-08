package ru.practicum.shareit.handler;

import org.springframework.data.domain.PageRequest;
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

public class GetRejected extends BookingStateHandler {
    @Override
    public BookingState getState() {
        return BookingState.REJECTED;
    }

    @Override
    public List<BookingDto> getBookings(Long senderId, UserType userType, PageRequest pageRequest) {
        List<Booking> bookings = new ArrayList<>();
        if (userType.equals(USER)) {
            bookings = bookingStorage.findAllByBookerIdAndStatus(senderId, getState().toString(), pageRequest);
        }
        if (userType.equals(OWNER)) {
            bookings = bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(senderId, getState().toString(), pageRequest);
        }
        return bookingMapper.fromListToDtoList(bookings);
    }

    public GetRejected(BookingStorage bookingStorage, BookingMapper bookingMapper) {
        super(bookingStorage, bookingMapper);
    }
}

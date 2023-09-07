package ru.practicum.shareit.handler;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.params.BookingState;
import ru.practicum.shareit.booking.params.UserType;
import ru.practicum.shareit.booking.storage.BookingStorage;

import java.util.ArrayList;
import java.util.List;

public abstract class BookingStateHandler {

    public BookingStateHandler next;

    public final BookingStorage bookingStorage;

    public final BookingMapper bookingMapper;

    protected BookingStateHandler(BookingStorage bookingStorage, BookingMapper bookingMapper) {
        this.bookingStorage = bookingStorage;
        this.bookingMapper = bookingMapper;
    }

    public abstract BookingState getState();

    public abstract List<BookingDto> getBookings(Long senderId, UserType userType);

    public static BookingStateHandler link(BookingStateHandler handler, BookingStateHandler... handlerNext) {
        BookingStateHandler head = handler;
        for (BookingStateHandler item : handlerNext) {
            head.next = item;
            head = item;
        }
        return handler;
    }

    public List<BookingDto> handle(Long senderId, BookingState state, UserType userType) {
        if (state.equals(getState())) {
            return getBookings(senderId, userType);
        }
        if (next == null) {
            return new ArrayList<>();
        }
        return next.handle(senderId, state, userType);
    }
}
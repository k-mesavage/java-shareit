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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GetCurrent extends BookingStateHandler {

    @Override
    public BookingState getState() {
        return BookingState.CURRENT;
    }

    @Override
    public List<BookingDto> getBookings(Long senderId, UserType userType, PageRequest pageRequest) {
        List<Booking> bookings = new ArrayList<>();
        if (userType.equals(USER)) {
            bookings = bookingStorage.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(senderId,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    pageRequest);
        }
        if (userType.equals(OWNER)) {
            bookings = bookingStorage
                    .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(senderId,
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            pageRequest);
        }
        return bookingMapper.fromListToDtoList(bookings);
    }

    public GetCurrent(BookingStorage bookingStorage, BookingMapper bookingMapper) {
        super(bookingStorage, bookingMapper);
    }
}

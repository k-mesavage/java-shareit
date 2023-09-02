package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.BookingItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.BookingUser;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingMapper {

    public BookingDto toDto(Booking booking, BookingItem item, BookingUser booker) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(item)
                .booker(booker)
                .status(booking.getStatus())
                .build();
    }

    public Booking fromDto(BookingDto bookingDto, User booker, Item item) {
        return Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .itemId(item.getId())
                .bookerId(booker.getId())
                .build();
    }
}

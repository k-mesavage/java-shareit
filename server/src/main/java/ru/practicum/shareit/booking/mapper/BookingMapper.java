package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingMapper {


    private final UserMapper userMapper;

    private final ItemMapper itemMapper;

    private final BookingStorage bookingStorage;

    public BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemMapper.toBooking(booking.getItem()))
                .booker(userMapper.toBooking(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public List<BookingDto> fromListToDtoList(List<Booking> bookings) {
        List<BookingDto> dtoBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            dtoBookings.add(toDto(booking));
        }
        return dtoBookings;
    }

    public ShortBookingDto toShortBookingDto(Booking booking) {
        return ShortBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    public ItemDto addShortBooking(ItemDto itemDto) {
        Booking lastBooking = bookingStorage
                .findFirstByItemIdAndStartIsBeforeOrderByStartDesc(itemDto.getId(),
                        LocalDateTime.now());
        Booking nextBooking = bookingStorage
                .getFirstByItemIdAndStatusAndStartIsAfterOrderByStartAsc(itemDto.getId(),
                        "APPROVED",
                        LocalDateTime.now());
        if (lastBooking != null) {
            itemDto.setLastBooking(toShortBookingDto(lastBooking));
        }
        if (nextBooking != null) {
            itemDto.setNextBooking(toShortBookingDto(nextBooking));
        }
        return itemDto;
    }
}

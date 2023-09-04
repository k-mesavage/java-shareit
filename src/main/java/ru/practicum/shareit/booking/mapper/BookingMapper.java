package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingMapper {

    private final UserStorage userStorage;

    private final UserMapper userMapper;

    private final ItemStorage itemStorage;

    private final ItemMapper itemMapper;

    private final BookingStorage bookingStorage;

    public BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemMapper.toBooking(itemStorage.getReferenceById(booking.getItemId())))
                .booker(userMapper.toBooking(userStorage.getReferenceById(booking.getBookerId())))
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
                .bookerId(booking.getBookerId())
                .build();
    }

    public ItemDto addShortBooking(ItemDto itemDto) {
        Booking lastBooking = bookingStorage
                .findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(itemDto.getId(),
                        LocalDateTime.now(),
                        "APPROVED");
        Booking nextBooking = bookingStorage
                .findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(itemDto.getId(),
                        LocalDateTime.now(),
                        "APPROVED");
        if (lastBooking != null) {
            itemDto.lastBooking = toShortBookingDto(lastBooking);
        }
        if (nextBooking != null) {
            itemDto.nextBooking = toShortBookingDto(nextBooking);
        }
        return itemDto;
    }
}

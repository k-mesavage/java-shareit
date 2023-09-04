package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ShortBookingItem;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.ShortUserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingMapper {

    private final UserStorage userStorage;

    private final UserMapper userMapper;

    private final ItemStorage itemStorage;

    private final ItemMapper itemMapper;

    public BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemMapper.toBooking(itemStorage.getReferenceById(booking.getItemId())))
                .booker(userMapper.toBooking(userStorage.getReferenceById(booking.getBookerId())))
                .status(booking.getStatus().toString())
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

    @Transactional
    public List<BookingDto> fromListToDtoList(List<Booking> bookings) {
        List<BookingDto> dtoBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            dtoBookings.add(toDto(booking));
        }
        return dtoBookings;
    }
}

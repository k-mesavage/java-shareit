package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

    @Mock
    private UserStorage userStorage;
    @Mock
    private ItemStorage itemStorage;
    @Mock
    private BookingStorage bookingStorage;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private BookingMapper bookingMapper;

    private final Item item = Item.builder()
            .id(1L).build();
    private final User user = new User(1L, "Name", "email@mail.com");
    private final Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .item(item)
            .booker(user)
            .status("WAITING").build();

    @Test
    void toDto() {
        final BookingDto expectedBooking = bookingMapper.toDto(booking);
        assertEquals(expectedBooking.getId(), 1L);
        assertEquals(expectedBooking.getStart(), booking.getStart());
        assertEquals(expectedBooking.getEnd(), booking.getEnd());
    }

    @Test
    void fromListToDtoList() {
        final List<BookingDto> actualBookings = List.of(BookingDto.builder()
                .id(1L)
                .status("APPROVED")
                .build());
        final List<Booking> bookings = List.of(booking);

        final List<BookingDto> expectedList = bookingMapper.fromListToDtoList(bookings);
        assertEquals(actualBookings.size(), expectedList.size());
        assertEquals(actualBookings.get(0).getId(), expectedList.get(0).getId());
        assertEquals(actualBookings.get(0).getStatus(), "APPROVED");

    }

    @Test
    void toShortBookingDto() {
        final ShortBookingDto expectedBooking = bookingMapper.toShortBookingDto(booking);

        assertEquals(expectedBooking.getId(), booking.getId());
        assertEquals(expectedBooking.getBookerId(), booking.getBooker().getId());
    }

    @Test
    void addShortBooking() {
        final ItemDto actualItem = ItemDto.builder()
                .id(1L)
                .requestId(1L)
                .name("Item Name").build();

        when(bookingStorage
                .findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(anyLong(), any(), anyString()))
                .thenReturn(booking);
        when(bookingStorage
                .findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(anyLong(), any(), anyString()))
                .thenReturn(booking);

        final ItemDto expectedItem = bookingMapper.addShortBooking(actualItem);
        assertEquals(expectedItem.getLastBooking().getId(), booking.getId());
        assertEquals(expectedItem.getNextBooking().getBookerId(), booking.getBooker().getId());
    }
}
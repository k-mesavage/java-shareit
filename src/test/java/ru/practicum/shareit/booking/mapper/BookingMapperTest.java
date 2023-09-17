package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

    @Mock
    private UserStorage userStorage;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ItemStorage itemStorage;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private BookingStorage bookingStorage;
    @InjectMocks
    private BookingMapper bookingMapper;

    private final Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .itemId(1L)
            .bookerId(1L)
            .status("WAITING").build();
    private final Item item = Item.builder()
            .id(1L).build();
    private final User user = new User(1L, "Name", "email@mail.com");

    @Test
    void toDto() {

        when(itemStorage.getReferenceById(anyLong()))
                .thenReturn(item);
        when(userStorage.getReferenceById(anyLong()))
                .thenReturn(user);

        BookingDto expectedBooking = bookingMapper.toDto(booking);
        assertEquals(expectedBooking.getId(), 1L);
        assertEquals(expectedBooking.getStart(), booking.getStart());
        assertEquals(expectedBooking.getEnd(), booking.getEnd());
    }

    @Test
    void fromListToDtoList() {

    }

    @Test
    void toShortBookingDto() {
    }

    @Test
    void addShortBooking() {
    }
}
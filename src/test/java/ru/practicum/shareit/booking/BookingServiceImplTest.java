package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.WorkingBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.params.UserType;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.ShortUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.utility.ObjectChecker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private UserStorage userStorage;
    @Mock
    private ItemStorage itemStorage;
    @Mock
    private BookingStorage bookingStorage;
    @Mock
    private ObjectChecker objectChecker;
    @Mock
    private BookingMapper bookingMapper;
    @InjectMocks
    private BookingServiceImpl bookingService;

    private final LocalDateTime start = LocalDateTime.now().plusSeconds(1);
    private final LocalDateTime end = LocalDateTime.now().plusDays(1);
    private final ShortUserDto shortUserDto1 = new ShortUserDto(1L);
    private final User user = new User(1L, "Name", "email@mail.com");
    private final Item item = Item.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(user)
            .build();
    private final ShortItemDto shortItemDto = ShortItemDto.builder()
            .id(1L)
            .name("Item")
            .build();
    private final Booking booking = Booking.builder()
            .id(1L)
            .status("APPROVE")
            .bookerId(1L)
            .itemId(1L)
            .start(start)
            .end(end)
            .build();
    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .start(start)
            .end(end)
            .status("approve")
            .booker(shortUserDto1)
            .item(shortItemDto)
            .build();
    private final WorkingBookingDto request = WorkingBookingDto.builder()
            .start(start)
            .end(end)
            .itemId(1L)
            .build();

    @Test
    void shouldAddBooking() {
        when(itemStorage.getReferenceById(anyLong()))
                .thenReturn(item);
        when(bookingMapper.toDto(any()))
                .thenReturn(bookingDto);

        BookingDto expectedBooking = bookingService.addBooking(1L, request);

        assertEquals(expectedBooking, bookingDto);
        verify(bookingStorage).save(any());
    }

    @Test
    void shouldRequestBooking() {
        when(bookingStorage.getReferenceById(anyLong()))
                .thenReturn(booking);
        when(itemStorage.getReferenceById(anyLong()))
                .thenReturn(item);
        when(userStorage.getReferenceById(anyLong()))
                .thenReturn(user);
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);
        BookingDto expectedBooking = bookingService.requestBooking(true, 1L, 1L);
        assertEquals(bookingDto, expectedBooking);
        verify(bookingStorage).save(any());
    }

    @Test
    void getAllBookingsByUser() {
        List<BookingDto> actualListOfBookings = new ArrayList<>();
        actualListOfBookings.add(bookingDto);

        when(bookingStorage.findAllByBookerIdAndStatus(1L, "WAITING", PageRequest.of(1,1)))
                .thenReturn(List.of(booking));
        when(bookingMapper.fromListToDtoList(any()))
                .thenReturn(actualListOfBookings);

        List<BookingDto> expectedList = bookingService
                .getAllBookingsByUser(1L,
                        "WAITING",
                        1,1);
        assertEquals(actualListOfBookings, expectedList);
    }

    @Test
    void getAllBookingsByUserWhenPageRequestAsWrong() {
        List<BookingDto> actualListOfBookings = new ArrayList<>();
        actualListOfBookings.add(bookingDto);

        lenient().when(bookingStorage.findAllByBookerIdAndStatus(1L, "WAITING", PageRequest.of(1,1)))
                .thenReturn(List.of(booking));
        lenient().when(bookingMapper.fromListToDtoList(any()))
                .thenReturn(actualListOfBookings);

        assertThrows(IllegalArgumentException.class,() -> bookingService
                .getAllBookingsByUser(1L,
                        "WAITING",
                        -1,1));
    }

    @Test
    void getAllItemsBookingByOwner() {
        List<BookingDto> actualListOfBookings = new ArrayList<>();
        actualListOfBookings.add(bookingDto);

        when(bookingService.getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                "WAITING",
                UserType.OWNER,
                PageRequest.of(1,1)))
                .thenReturn(actualListOfBookings);

        List<BookingDto> expectedList = bookingService
                .getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                        "WAITING",
                        UserType.OWNER,
                        PageRequest.of(1, 1));
        assertEquals(actualListOfBookings, expectedList);
    }

    @Test
    void getBookingById() {
        when(bookingStorage.getReferenceById(1L)).thenReturn(booking);
        when(itemStorage.getReferenceById(1L)).thenReturn(item);
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        BookingDto expectedBooking = bookingService.getBookingById(1L, 1L);
        assertEquals(bookingDto, expectedBooking);
    }
}
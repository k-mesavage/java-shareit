package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Nested;
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
            .status("APPROVED")
            .booker(user)
            .item(item)
            .start(start)
            .end(end)
            .build();
    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .start(start)
            .end(end)
            .status("APPROVED")
            .booker(shortUserDto1)
            .item(shortItemDto)
            .build();
    private final WorkingBookingDto request = WorkingBookingDto.builder()
            .start(start)
            .end(end)
            .itemId(1L)
            .build();

    @Nested
    class CreateTests {
        @Test
        void shouldAddBooking() {
            when(itemStorage.getReferenceById(anyLong()))
                    .thenReturn(item);
            when(bookingMapper.toDto(any()))
                    .thenReturn(bookingDto);

            final BookingDto expectedBooking = bookingService.addBooking(1L, request);

            assertEquals(expectedBooking, bookingDto);
            verify(bookingStorage).save(any());
        }
    }

    @Nested
    class RequestTests {
        @Test
        void shouldRequestBooking() {
            when(bookingStorage.getReferenceById(anyLong()))
                    .thenReturn(booking);
            when(itemStorage.getReferenceById(anyLong()))
                    .thenReturn(item);
            when(userStorage.getReferenceById(anyLong()))
                    .thenReturn(user);
            when(bookingMapper.toDto(booking)).thenReturn(bookingDto);
            final BookingDto expectedBooking = bookingService.updateBooking(true, 1L, 1L);
            assertEquals(bookingDto, expectedBooking);
            verify(bookingStorage).save(any());
        }

        @Test
        void shouldRejectBooking() {
            when(bookingStorage.getReferenceById(anyLong()))
                    .thenReturn(booking);
            when(itemStorage.getReferenceById(anyLong()))
                    .thenReturn(item);
            when(userStorage.getReferenceById(anyLong()))
                    .thenReturn(user);
            when(bookingMapper.toDto(booking)).thenReturn(bookingDto);
            final BookingDto expectedBooking = bookingService.updateBooking(false, 1L, 1L);
            assertEquals(bookingDto, expectedBooking);
            verify(bookingStorage).save(any());
        }
    }

    @Nested
    class GetTest {
        @Test
        void getAllWaitingBookingsByUser() {
            final List<BookingDto> actualListOfBookings = List.of(bookingDto);

            when(bookingStorage.findAllByBookerIdAndStatus(1L, "WAITING", PageRequest.of(1,1)))
                    .thenReturn(List.of(booking));
            when(bookingMapper.fromListToDtoList(any()))
                    .thenReturn(actualListOfBookings);

            final List<BookingDto> expectedList = bookingService
                    .getAllBookingsByUser(UserType.USER, 1L,
                            "WAITING",
                            1,1);
            assertEquals(actualListOfBookings, expectedList);
        }

        @Test
        void getCurrentBookingsByUser() {
            final List<BookingDto> actualListOfBookings = new ArrayList<>();
            actualListOfBookings.add(bookingDto);

            when(bookingService.getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                    "CURRENT",
                    UserType.USER,
                    PageRequest.of(1,1)))
                    .thenReturn(actualListOfBookings);

            final List<BookingDto> expectedList = bookingService
                    .getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                            "CURRENT",
                            UserType.USER,
                            PageRequest.of(1, 1));
            assertEquals(actualListOfBookings, expectedList);
        }

        @Test
        void getPastBookingsByUser() {
            final List<BookingDto> actualListOfBookings = new ArrayList<>();
            actualListOfBookings.add(bookingDto);

            when(bookingService.getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                    "PAST",
                    UserType.USER,
                    PageRequest.of(1,1)))
                    .thenReturn(actualListOfBookings);

            final List<BookingDto> expectedList = bookingService
                    .getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                            "PAST",
                            UserType.USER,
                            PageRequest.of(1, 1));
            assertEquals(actualListOfBookings, expectedList);
        }

        @Test
        void getFutureBookingsByUser() {
            final List<BookingDto> actualListOfBookings = new ArrayList<>();
            actualListOfBookings.add(bookingDto);

            when(bookingService.getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                    "FUTURE",
                    UserType.USER,
                    PageRequest.of(1,1)))
                    .thenReturn(actualListOfBookings);

            final List<BookingDto> expectedList = bookingService
                    .getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                            "FUTURE",
                            UserType.USER,
                            PageRequest.of(1, 1));
            assertEquals(actualListOfBookings, expectedList);
        }

        @Test
        void getAllWaitingItemsBookingByOwner() {
            final List<BookingDto> actualListOfBookings = new ArrayList<>();
            actualListOfBookings.add(bookingDto);

            when(bookingService.getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                    "WAITING",
                    UserType.OWNER,
                    PageRequest.of(1,1)))
                    .thenReturn(actualListOfBookings);

            final List<BookingDto> expectedList = bookingService
                    .getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                            "WAITING",
                            UserType.OWNER,
                            PageRequest.of(1, 1));
            assertEquals(actualListOfBookings, expectedList);
        }

        @Test
        void getCurrentItemsBookingByOwner() {
            final List<BookingDto> actualListOfBookings = new ArrayList<>();
            actualListOfBookings.add(bookingDto);

            when(bookingService.getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                    "CURRENT",
                    UserType.OWNER,
                    PageRequest.of(1,1)))
                    .thenReturn(actualListOfBookings);

            final List<BookingDto> expectedList = bookingService
                    .getAllBookingsByUser(UserType.OWNER, 1L,
                            "CURRENT",
                            1, 1);
            assertEquals(actualListOfBookings, expectedList);
        }

        @Test
        void getPastItemsBookingByOwner() {
            final List<BookingDto> actualListOfBookings = new ArrayList<>();
            actualListOfBookings.add(bookingDto);

            when(bookingService.getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                    "PAST",
                    UserType.OWNER,
                    PageRequest.of(1,1)))
                    .thenReturn(actualListOfBookings);

            final List<BookingDto> expectedList = bookingService
                    .getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                            "PAST",
                            UserType.OWNER,
                            PageRequest.of(1, 1));
            assertEquals(actualListOfBookings, expectedList);
        }

        @Test
        void getFutureItemsBookingByOwner() {
            final List<BookingDto> actualListOfBookings = new ArrayList<>();
            actualListOfBookings.add(bookingDto);

            when(bookingService.getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                    "FUTURE",
                    UserType.OWNER,
                    PageRequest.of(1,1)))
                    .thenReturn(actualListOfBookings);

            final List<BookingDto> expectedList = bookingService
                    .getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                            "FUTURE",
                            UserType.OWNER,
                            PageRequest.of(1, 1));
            assertEquals(actualListOfBookings, expectedList);
        }

        @Test
        void getRejectedItemsBookingByOwner() {
            final List<BookingDto> actualListOfBookings = new ArrayList<>();
            actualListOfBookings.add(bookingDto);

            when(bookingService.getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                    "REJECTED",
                    UserType.OWNER,
                    PageRequest.of(1,1)))
                    .thenReturn(actualListOfBookings);

            final List<BookingDto> expectedList = bookingService
                    .getAllBookingsForUserOrOwnerByUserIdAndState(1L,
                            "REJECTED",
                            UserType.OWNER,
                            PageRequest.of(1, 1));
            assertEquals(actualListOfBookings, expectedList);
        }

        @Test
        void getBookingById() {
            when(bookingStorage.getReferenceById(1L)).thenReturn(booking);
            when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

            final BookingDto expectedBooking = bookingService.getBookingById(1L, 1L);
            assertEquals(bookingDto, expectedBooking);
        }
    }
}
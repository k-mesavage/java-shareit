package ru.practicum.shareit.utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.WorkingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObjectCheckerTest {

    @Mock
    BookingStorage bookingStorage;
    @Mock
    private UserStorage userStorage;
    @Mock
    private ItemStorage itemStorage;
    @InjectMocks
    ObjectChecker objectChecker;

    @Test
    void userNotFound() {
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(ObjectNotFoundException.class,
                () -> objectChecker.userFound(anyLong()));
    }

    @Test
    void userNotAccess() {
        assertThrows(ObjectNotFoundException.class,
                () -> objectChecker.userAccess(1L, 2L));
    }

    @Test
    void reApprove() {
        assertThrows(BadRequestException.class,
                () -> objectChecker.reApprove(Booking.builder()
                        .status("APPROVED").build()));
    }

    @Test
    void bookerAccess() {
        assertThrows(ObjectNotFoundException.class,
                () -> objectChecker.bookerAccess(1L, 1L));
    }

    @Test
    void itemFound() {
        when(itemStorage.existsById(anyLong()))
                .thenReturn(false);

        assertThrows(ObjectNotFoundException.class,
                () -> objectChecker.itemFound(anyLong()));
    }

    @Test
    void itemAvailable() {
        Item item = Item.builder()
                .id(1L)
                .available(false).build();

        when(itemStorage.getReferenceById(anyLong()))
                .thenReturn(item);

        assertThrows(BadRequestException.class,
                () -> objectChecker.itemAvailable(anyLong()));
    }

    @Test
    void checkBookingDate() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().minusMinutes(1))
                .end(LocalDateTime.now().plusDays(1)).build();
        List<Booking> checkingBookings = List.of(booking);
        WorkingBookingDto bookingDto = WorkingBookingDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(3)).build();
        WorkingBookingDto booking1 = WorkingBookingDto.builder()
                .start(LocalDateTime.of(1999, 12, 12, 1,1))
                .end(LocalDateTime.of(1999, 12, 12, 1,1)).build();

        when(bookingStorage.findAllByItemId(1L))
                .thenReturn(checkingBookings);

        assertThrows(BadRequestException.class,
                () -> objectChecker.checkBookingDate(bookingDto, 1L));
        assertThrows(BadRequestException.class,
                () -> objectChecker.checkBookingDate(WorkingBookingDto.builder()
                        .start(LocalDateTime.now())
                        .end(LocalDateTime.now()).build(), 1L));
        assertThrows(BadRequestException.class,
                () -> objectChecker.checkBookingDate(WorkingBookingDto.builder()
                        .start(LocalDateTime.now())
                        .end(LocalDateTime.now().minusDays(1)).build(), 1L));
        assertThrows(BadRequestException.class,
                () -> objectChecker.checkBookingDate(booking1, 1L));
    }

    @Test
    void checkDateTime() {
        WorkingBookingDto booking = WorkingBookingDto.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1)).build();
        WorkingBookingDto booking1 = WorkingBookingDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now()).build();

        assertThrows(BadRequestException.class,
                () -> objectChecker.checkDateTime(booking));
        assertThrows(BadRequestException.class,
                () -> objectChecker.checkDateTime(booking1));
    }

    @Test
    void bookingFound() {
        when(bookingStorage.findFirstByItemIdAndBookerIdAndStatusAndEndBefore(anyLong(), anyLong(), anyString(), any()))
                .thenReturn(null);

        assertThrows(BadRequestException.class,
                () -> objectChecker.bookingFound(1L, 1L));
    }

    @Test
    void pageRequestLegal() {
        int from = -1;
        int size = 0;
        int from2 = 0;
        int size2 = -1;

        assertThrows(IllegalArgumentException.class,
                () -> objectChecker.pageRequestLegal(from, size));
        assertThrows(IllegalArgumentException.class,
                () -> objectChecker.pageRequestLegal(from2, size2));
    }
}
package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookingStorageTest {

    @Autowired
    private BookingStorage bookingStorage;
    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserStorage userStorage;

    @BeforeEach
    void beforeEach() {
        bookingStorage.deleteAll();
        final User user = userStorage.save(new User(1L, "name", "user@email.com"));
        final Item item = itemStorage.save(Item.builder().id(1L).owner(user).build());
        final Booking booking = bookingStorage.save(Booking.builder().id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .bookerId(1L)
                .status("APPROVED")
                .itemId(1L)
                .build());
        final Booking booking2 = bookingStorage.save(Booking.builder()
                .bookerId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .status("APPROVED")
                .build());
        final Booking booking3 = bookingStorage.save(Booking.builder()
                .bookerId(1L)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(3))
                .itemId(1L)
                .status("REJECTED")
                .build());
    }

    @Test
    void findAllByItemId() {
        List<Booking> expectedBookingList = bookingStorage.findAllByItemId(1L);
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
        List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdOrderByStartDesc(1L, Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    void findAllByBookerIdAndEndBeforeOrderByStartDesc() {
        List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdAndEndBeforeOrderByStartDesc(1L,
                LocalDateTime.now().plusDays(10),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdAndEndBeforeOrderByStartDesc(1L,
                LocalDateTime.now().plusDays(10),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    void findAllByBookerIdAndStatusFuture() {
        List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdAndStatusFuture(1L,
                LocalDateTime.now().minusDays(1),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    void findAllByBookerIdAndStatus() {
        List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdAndStatus(1L,
                "APPROVED",
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 2);
    }

    @Test
    void findAllByItemOwnerIdOrderByStartDesc() {
        List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdOrderByStartDesc(1L,
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    void findAllByItemOwnerIdAndEndBeforeOrderByStartDesc() {
        List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(1L,
                LocalDateTime.now().plusDays(3), Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    void findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(1L,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now(),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    void findAllByItemOwnerIdAndStatusFuture() {
        List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdAndStatusFuture(1L,
                LocalDateTime.now().minusDays(1),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    void findAllByItemOwnerIdAndStatusOrderByStartDesc() {
        List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(1L,
                "APPROVED", Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 2);
    }

    @Test
    void findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc() {
        Booking expectedBooking = bookingStorage.findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(1L,
                LocalDateTime.now(), "APPROVED");
        assertEquals(expectedBooking.getId(), 13L);
    }

    @Test
    void findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc() {
        Booking expectedBooking = bookingStorage.findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(1L,
                LocalDateTime.now(), "APPROVED");
        assertEquals(expectedBooking.getId(), 1L);
    }

    @Test
    void findFirstByItemIdAndBookerIdAndStatusAndEndBefore() {
        Booking expectedBooking = bookingStorage.findFirstByItemIdAndBookerIdAndStatusAndEndBefore(1L,
                1L,
                "APPROVED",
                LocalDateTime.now().plusDays(1));
        assertEquals(expectedBooking.getId(), 19L);
    }
}
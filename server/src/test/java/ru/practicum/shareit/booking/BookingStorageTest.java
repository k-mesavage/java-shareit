package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingStorageTest {

    @Autowired
    private BookingStorage bookingStorage;
    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserStorage userStorage;

    private User user;
    private Item item;
    private Booking booking;
    private Booking booking2;
    private Booking booking3;

    @BeforeEach
    void beforeEach() {
        user = userStorage.save(User.builder().name("name").email("usersf@email.com").build());
        item = itemStorage.save(Item.builder().name("item name").description("desc").available(true).owner(user).build());
        booking = bookingStorage.save(Booking.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .booker(user)
                .status("APPROVED")
                .item(item)
                .build());
        booking2 = bookingStorage.save(Booking.builder()
                .booker(user)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .status("APPROVED")
                .build());
        booking3 = bookingStorage.save(Booking.builder()
                .booker(user)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(3))
                .item(item)
                .status("REJECTED")
                .build());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByItemId() {
        final List<Booking> expectedBookingList = bookingStorage.findAllByItemId(1L);
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByBookerIdOrderByStartDesc() {
        final List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdOrderByStartDesc(1L, Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByBookerIdAndEndBeforeOrderByStartDesc() {
        final List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdAndEndBeforeOrderByStartDesc(1L,
                LocalDateTime.now().plusDays(10),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        final List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdAndEndBeforeOrderByStartDesc(1L,
                LocalDateTime.now().plusDays(10),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByBookerIdAndStatusFuture() {
        final List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdAndStatusFuture(1L,
                LocalDateTime.now().minusDays(1),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByBookerIdAndStatus() {
        final List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdAndStatus(1L,
                "APPROVED",
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 2);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByItemOwnerIdOrderByStartDesc() {
        final List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdOrderByStartDesc(1L,
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByItemOwnerIdAndEndBeforeOrderByStartDesc() {
        final List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(1L,
                LocalDateTime.now().plusDays(3), Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        final List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(1L,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now(),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByItemOwnerIdAndStatusFuture() {
        final List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdAndStatusFuture(1L,
                LocalDateTime.now().minusDays(1),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByItemOwnerIdAndStatusOrderByStartDesc() {
        final List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(1L,
                "APPROVED", Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 2);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc() {
        final Booking expectedBooking = bookingStorage.findFirstByItemIdAndStartIsBeforeOrderByStartDesc(1L,
                LocalDateTime.now());
        assertEquals(expectedBooking.getId(), 1L);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc() {
        final Booking expectedBooking = bookingStorage.getFirstByItemIdAndStatusAndStartIsAfterOrderByStartAsc(1L,
                "APPROVED", LocalDateTime.now());
        assertEquals(expectedBooking.getId(), 2L);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findFirstByItemIdAndBookerIdAndStatusAndEndBefore() {
        final Booking expectedBooking = bookingStorage.findFirstByItemIdAndBookerIdAndStatusAndEndBefore(1L,
                1L,
                "APPROVED",
                LocalDateTime.now().plusDays(1));
        assertEquals(expectedBooking.getId(), 1L);
    }
}
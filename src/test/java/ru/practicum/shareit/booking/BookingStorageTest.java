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
        user = User.builder().name("name").email("usersf@email.com").build();
        item = Item.builder().name("item name").description("desc").available(true).owner(user).build();
        booking = bookingStorage.save(Booking.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .booker(user)
                .status("APPROVED")
                .item(item)
                .build());
        booking2 = Booking.builder()
                .booker(user)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .status("APPROVED")
                .build();
        booking3 = Booking.builder()
                .booker(user)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(3))
                .item(item)
                .status("REJECTED")
                .build();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByItemId() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        List<Booking> expectedBookingList = bookingStorage.findAllByItemId(1L);
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByBookerIdOrderByStartDesc() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdOrderByStartDesc(1L, Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByBookerIdAndEndBeforeOrderByStartDesc() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdAndEndBeforeOrderByStartDesc(1L,
                LocalDateTime.now().plusDays(10),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdAndEndBeforeOrderByStartDesc(1L,
                LocalDateTime.now().plusDays(10),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByBookerIdAndStatusFuture() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdAndStatusFuture(1L,
                LocalDateTime.now().minusDays(1),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByBookerIdAndStatus() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        List<Booking> expectedBookingList = bookingStorage.findAllByBookerIdAndStatus(1L,
                "APPROVED",
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 2);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByItemOwnerIdOrderByStartDesc() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdOrderByStartDesc(1L,
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByItemOwnerIdAndEndBeforeOrderByStartDesc() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(1L,
                LocalDateTime.now().plusDays(3), Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(1L,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now(),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByItemOwnerIdAndStatusFuture() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdAndStatusFuture(1L,
                LocalDateTime.now().minusDays(1),
                Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByItemOwnerIdAndStatusOrderByStartDesc() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        List<Booking> expectedBookingList = bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(1L,
                "APPROVED", Pageable.unpaged());
        assertEquals(expectedBookingList.size(), 2);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        Booking expectedBooking = bookingStorage.findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(1L,
                LocalDateTime.now(), "APPROVED");
        assertEquals(expectedBooking.getId(), 1L);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        Booking expectedBooking = bookingStorage.findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(1L,
                LocalDateTime.now(), "APPROVED");
        assertEquals(expectedBooking.getId(), 1L);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findFirstByItemIdAndBookerIdAndStatusAndEndBefore() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
        Booking expectedBooking = bookingStorage.findFirstByItemIdAndBookerIdAndStatusAndEndBefore(1L,
                1L,
                "APPROVED",
                LocalDateTime.now().plusDays(1));
        assertEquals(expectedBooking.getId(), 1L);
    }
}
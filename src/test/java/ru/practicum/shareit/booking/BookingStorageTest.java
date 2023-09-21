package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
    private final Booking booking1 = Booking.builder()
            .itemId(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(1))
            .bookerId(1L)
            .build();
    private final Booking booking2 = Booking.builder()
            .itemId(1L)
            .start(LocalDateTime.now().plusDays(1))
            .end(LocalDateTime.now().plusDays(2))
            .bookerId(1L)
            .build();
    private final Booking booking3 = Booking.builder()
            .itemId(1L)
            .start(LocalDateTime.now().plusDays(2))
            .end(LocalDateTime.now().plusDays(3))
            .bookerId(1L)
            .build();
    private final Item item = Item.builder().id(1L).name("Item name").build();
    private final User user = new User(1L, "User name", "User@email.com");

    @BeforeEach
    void beforeAll() {
        userStorage.save(user);
        itemStorage.save(item);
        bookingStorage.save(booking1);
        bookingStorage.save(booking2);
        bookingStorage.save(booking3);
    }

    @Test
    void findAllByItemId() {
        List<Booking> expectedBookingList = bookingStorage.findAllByItemId(1L);
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
        List<Booking> expectedBookingList = bookingStorage.findAllByItemId(1L);
        assertEquals(expectedBookingList.size(), 3);
    }

    @Test
    void findAllByBookerIdAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
    }

    @Test
    void findAllByBookerIdAndStatusFuture() {
    }

    @Test
    void findAllByBookerIdAndStatus() {
    }

    @Test
    void findAllByItemOwnerIdOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerIdAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
    }

    @Test
    void findAllByItemOwnerIdAndStatusFuture() {
    }

    @Test
    void findAllByItemOwnerIdAndStatusOrderByStartDesc() {
    }

    @Test
    void findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc() {
    }

    @Test
    void findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc() {
    }

    @Test
    void findFirstByItemIdAndBookerIdAndStatusAndEndBefore() {
    }
}
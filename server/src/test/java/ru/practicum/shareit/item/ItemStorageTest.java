package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemStorageTest {

    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private ItemRequestStorage requestStorage;

    private User user;
    private User user1;
    private Item item;
    private Item item1;
    private ItemRequest request;

    @BeforeEach
    void beforeEach() {
        user = userStorage.save(User.builder()
                .id(1L)
                .name("name")
                .email("user@email.com").build());
        user1 = userStorage.save(User.builder()
                .id(2L)
                .name("name")
                .email("surser@email.com").build());
        item = itemStorage.save(Item.builder()
                .id(1L)
                .name("item name")
                .description("desc")
                .available(true)
                .owner(user).build());
        request = requestStorage.save(ItemRequest.builder()
                .id(1L)
                .description("desc")
                .requesterId(2L)
                .created(LocalDateTime.now())
                .build());
        item1 = itemStorage.save(Item.builder()
                .id(2L)
                .name("item name")
                .description("desc")
                .available(true)
                .requestId(1L)
                .owner(user).build());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByOwnerId() {
        final List<Item> expectedList = itemStorage.findAllByOwnerId(1L, Pageable.unpaged());
        assertEquals(expectedList.get(0).getOwner(), user);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void searchAvailableItems() {
        final List<Item> expectedList = itemStorage.searchAvailableItems("item");
        assertEquals(expectedList.size(), 2);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByRequestIdOrderByIdDesc() {
        final List<Item> expectedList = itemStorage.findAllByRequestIdOrderByIdDesc(1L);
        assertEquals(expectedList.size(), 1);
    }
}
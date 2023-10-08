package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestStorageTest {

    @Autowired
    private UserStorage userStorage;
    @Autowired
    private ItemRequestStorage requestStorage;

    private User user;
    private ItemRequest request;
    private ItemRequest request1;

    @BeforeEach
    void beforeEach() {
        user = userStorage.save(User.builder()
                .id(1L)
                .name("name")
                .email("user@email.com").build());
        request = requestStorage.save(ItemRequest.builder()
                .id(1L)
                .description("desc")
                .requesterId(1L)
                .created(LocalDateTime.now())
                .build());
        request = requestStorage.save(ItemRequest.builder()
                .id(2L)
                .description("desc")
                .requesterId(1L)
                .created(LocalDateTime.now().plusHours(1))
                .build());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByRequesterIdOrderByCreatedDesc() {
        final List<ItemRequest> expectedList = requestStorage
                .findAllByRequesterIdOrderByCreatedDesc(1L, Pageable.unpaged());
        assertEquals(expectedList.get(0).getId(), 2L);
        assertEquals(expectedList.get(1).getId(), 1L);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getAll() {
        final List<ItemRequest> expectedList = requestStorage
                .getAll(Pageable.unpaged());
        assertEquals(expectedList.size(), 2);
    }
}
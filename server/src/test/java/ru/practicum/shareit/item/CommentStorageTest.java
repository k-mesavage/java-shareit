package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentStorageTest {

    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private CommentStorage commentStorage;

    private User user;
    private Item item;
    private Comment comment;

    @BeforeEach
    void beforeEach() {
        user = userStorage.save(User.builder()
                .name("name")
                .email("usersf@email.com").build());
        item = itemStorage.save(Item.builder()
                .id(1L)
                .name("item name")
                .description("desc")
                .available(true)
                .owner(user).build());
        comment = commentStorage.save(Comment.builder()
                .text("Comment")
                .author(user)
                .item(item).build());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void findAllByItemId() {
        final List<Comment> expectedList = commentStorage.findAllByItemId(1L);
        assertEquals(expectedList.get(0).getText(), "Comment");
    }
}
package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.utility.ObjectChecker;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemStorage itemStorage;
    @MockBean
    private UserStorage userStorage;
    @MockBean
    private CommentStorage commentStorage;
    @MockBean
    private BookingMapper bookingMapper;
    @MockBean
    private ItemMapper itemMapper;
    @MockBean
    private CommentMapper commentMapper;
    @MockBean
    private ObjectChecker objectChecker;
    @MockBean
    ItemController itemController;
    @MockBean
    ItemService itemService;

    private static final String HEADER = "X-Sharer-User-Id";
    private final User user = new User(1L, "User name", "email@mail.com");
    private final Item actualItem = Item.builder()
            .id(1L)
            .owner(user)
            .name("Item")
            .description("Item description")
            .available(true)
            .build();
    private final ItemDto actualItemDto = ItemDto.builder()
            .id(1L)
            .name("Item")
            .description("Item description")
            .build();
    private final List<ItemDto> actualItemList = List.of(actualItemDto);
    private final CommentDto actualCommentDto = CommentDto.builder()
            .id(1L)
            .text("Text of comment")
            .authorName("Author name")
            .build();
    private final Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .itemId(1L)
            .bookerId(1L)
            .status("WAITING").build();


    @Test
    void getItemInformation() throws Exception {
        ItemDto actualItemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Item description")
                .build();
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(actualItemDto);

        MvcResult result = mvc.perform(get("/items/{itemId}", 1L)
                .header(HEADER, 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    @SneakyThrows
    void getOwnerItems() {
        when(itemController.getOwnerItems(anyLong()))
                .thenReturn(actualItemList);

        mvc.perform(get("/items")
                .header(HEADER, 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(actualItemDto.getId()), Long.class));

    }

    @Test
    @SneakyThrows
    void searchItems() {
        when(itemController.searchItems(anyString()))
                .thenReturn(actualItemList);

        mvc.perform(get("/items/search")
                .param("text", "Some text")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(actualItemDto.getId()), Long.class));
    }

    @Test
    @SneakyThrows
    void addItem() {
        when(itemController.addItem(anyLong(), any()))
                .thenReturn(actualItemDto);

        mvc.perform(post("/items")
                .header(HEADER, 1L)
                .content(mapper.writeValueAsString(actualItemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(actualCommentDto.getId()), Long.class));
    }

    @Test
    @SneakyThrows
    void updateItem() {
        when(itemController.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(actualItemDto);

        mvc.perform(patch("/items/{itemId}", 1L)
                        .header(HEADER, 1L)
                        .content(mapper.writeValueAsString(actualItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(actualCommentDto.getId()), Long.class));
    }

    @Test
    @SneakyThrows
    void deleteItem() {
        mvc.perform(delete("/items/{itemId}", 1L)
                        .header(HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void addComment() {
        when(itemController.addComment(anyLong(), anyLong(), any()))
                .thenReturn(actualCommentDto);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                .header(HEADER, 1L)
                .content(mapper.writeValueAsString(actualCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(actualCommentDto.getId()), Long.class));
    }
}
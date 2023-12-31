package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utility.HttpHeader;

import java.nio.charset.StandardCharsets;
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
    private ItemService itemService;

    private static final String HEADER = HttpHeader.X_SHARER_USER_ID;
    private static final ItemDto actualItemDto = ItemDto.builder()
            .id(1L)
            .name("Item")
            .description("Item description")
            .available(true)
            .build();
    private static final List<ItemDto> actualItemList = List.of(actualItemDto);
    private static final CommentDto actualCommentDto = CommentDto.builder()
            .id(1L)
            .text("Text of comment")
            .authorName("Author name")
            .build();

    @Nested
    class CreateTests {
        @Test
        void addItem() throws Exception {
            when(itemService.addItem(anyLong(), any()))
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
        void updateItem() throws Exception {
            when(itemService.updateItem(anyLong(), anyLong(), any()))
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
        void addComment() throws Exception {
            when(itemService.addComment(anyLong(), anyLong(), any()))
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

    @Nested
    class GetTest {
        @Test
        void getItemInformation() throws Exception {
            when(itemService.getItemById(anyLong(), anyLong()))
                    .thenReturn(actualItemDto);

            mvc.perform(get("/items/{itemId}", 1L)
                            .header(HEADER, 1L)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(actualItemDto.getId()), Long.class));
        }

        @Test
        void getOwnerItems() throws Exception {
            when(itemService.getAllItemsByUserId(anyLong(), anyInt(), anyInt()))
                    .thenReturn(actualItemList);

            mvc.perform(get("/items")
                            .header(HEADER, 1L)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.[0].id", is(actualItemDto.getId()), Long.class));

        }

        @Test
        void searchItems() throws Exception {
            when(itemService.searchItems(anyString()))
                    .thenReturn(actualItemList);

            mvc.perform(get("/items/search")
                            .param("text", "Some text")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.[0].id", is(actualItemDto.getId()), Long.class));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void deleteItem() throws Exception {
            mvc.perform(delete("/items/{itemId}", 1L)
                            .header(HEADER, 1L)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }
}
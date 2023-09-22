package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService itemRequestService;

    private static final String HEADER = "X-Sharer-User-Id";
    private static final ItemRequestDto actualItemRequestDto = ItemRequestDto.builder()
            .id(1L)
            .description("Actual request")
            .requesterId(1L)
            .items(List.of(ItemDto.builder().id(1L)
                    .name("Item")
                    .available(true)
                    .build()))
            .build();
    private static final List<ItemRequestDto> actualItems = List.of(actualItemRequestDto);

    @Test
    @SneakyThrows
    void addNewRequest() {
        when(itemRequestService.addItemRequest(anyLong(), any()))
                .thenReturn(actualItemRequestDto);

        mvc.perform(post("/requests")
                .header(HEADER, 1L)
                .content(objectMapper.writeValueAsString(actualItemRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(actualItemRequestDto.getId()), Long.class));
    }

    @Test
    @SneakyThrows
    void getRequestById() {
        when(itemRequestService.getItemRequestById(anyLong(), anyLong()))
                .thenReturn(actualItemRequestDto);

        mvc.perform(get("/requests/{requestId}", 1L)
                .header(HEADER, 1L)
                        .content(objectMapper.writeValueAsString(actualItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(actualItemRequestDto.getId()), Long.class));
    }

    @Test
    @SneakyThrows
    void getAllUserRequests() {
        when(itemRequestService.getAllItemRequestsByUser(anyLong()))
                .thenReturn(actualItems);

        mvc.perform(get("/requests")
                .header(HEADER, 1L)
                .content(objectMapper.writeValueAsString(actualItemRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", Matchers.is(actualItemRequestDto.getId()), Long.class));
    }

    @Test
    @SneakyThrows
    void getAllRequests() {
        when(itemRequestService.getAllItemRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(actualItems);

        mvc.perform(get("/requests/all")
                        .header(HEADER, 1L)
                        .content(objectMapper.writeValueAsString(actualItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", Matchers.is(actualItemRequestDto.getId()), Long.class));
    }
}
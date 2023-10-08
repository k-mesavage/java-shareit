package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestMapperTest {

    @Mock
    private ItemStorage itemStorage;
    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private RequestMapper requestMapper;

    private static final ItemRequest actualItemRequest = ItemRequest.builder()
            .id(1L)
            .description("Desc")
            .requesterId(1L)
            .created(LocalDateTime.now())
            .build();
    private static final ItemRequestDto actualItemRequestDto = ItemRequestDto.builder()
            .id(1L)
            .description("Desc")
            .requesterId(1L)
            .created(LocalDateTime.now())
            .items(List.of())
            .build();

    @Test
    void toItemRequestDto() {
        final ItemRequestDto expectedRequest = requestMapper.toItemRequestDto(actualItemRequest);
        assertEquals(expectedRequest.getId(), actualItemRequest.getId());
        assertEquals(expectedRequest.getRequesterId(), actualItemRequest.getRequesterId());
        assertEquals(expectedRequest.getCreated(), actualItemRequest.getCreated());
    }

    @Test
    void addItemsToRequest() {
        when(itemStorage.findAllByRequestIdOrderByIdDesc(anyLong()))
                .thenReturn(List.of());
        when(itemMapper.toItemDtoList(anyList()))
                .thenReturn(List.of(ItemDto.builder()
                        .id(1L)
                        .build()));
        requestMapper.addItemsToRequest(actualItemRequestDto);
        assertEquals(actualItemRequestDto.getItems().get(0).getId(), 1L);
    }

    @Test
    void fromItemRequestDto() {
        final ItemRequest expectedRequest = requestMapper.fromItemRequestDto(actualItemRequestDto);
        assertEquals(expectedRequest.getId(), actualItemRequestDto.getId());
        assertEquals(expectedRequest.getRequesterId(), actualItemRequestDto.getRequesterId());
        assertEquals(expectedRequest.getCreated(), actualItemRequestDto.getCreated());
    }

    @Test
    void fromListToItemRequestList() {
        final List<ItemRequestDto> expectedRequestsList = requestMapper.fromListToItemRequestList(List.of(actualItemRequest));
        assertEquals(expectedRequestsList.get(0).getId(), actualItemRequest.getId());
        assertEquals(expectedRequestsList.get(0).getDescription(), "Desc");
    }

    @Test
    void updateRequesterIdAndCreationDate() {
        final LocalDateTime actualCreated = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        requestMapper.updateRequesterIdAndCreationDate(2L, actualItemRequest);
        assertEquals(actualItemRequest.getCreated().truncatedTo(ChronoUnit.MINUTES), actualCreated);
        assertEquals(actualItemRequest.getRequesterId(), 2L);
    }
}
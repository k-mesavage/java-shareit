package ru.practicum.shareit.request;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.utility.ObjectChecker;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private RequestMapper requestMapper;
    @Mock
    private ObjectChecker objectChecker;
    @Mock
    private ItemRequestStorage itemRequestStorage;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private static final ItemRequest actualItemRequest = ItemRequest.builder()
            .id(1L)
            .description("Desc")
            .requesterId(1L)
            .build();
    private static final ItemRequestDto actualItemRequestDto = ItemRequestDto.builder()
            .id(1L)
            .items(List.of(ItemDto.builder().id(1L).name("Item").build()))
            .description("Desc")
            .requesterId(1L)
            .build();

    @Nested
    class CreateTests {
        @Test
        void addItemRequest() {
            when(requestMapper.fromItemRequestDto(any()))
                    .thenReturn(actualItemRequest);
            when(requestMapper.toItemRequestDto(any()))
                    .thenReturn(actualItemRequestDto);

            ItemRequestDto expectedRequest = itemRequestService.addItemRequest(1L, actualItemRequestDto);
            assertEquals(expectedRequest.getId(), actualItemRequestDto.getId());
            verify(itemRequestStorage, times(1)).save(any());
        }
    }

    @Nested
    class GetTest {
        @Test
        void getAllItemRequestsByUser() {
            itemRequestService.getAllItemRequestsByUser(1L);
            verify(itemRequestStorage, times(1)).findAllByRequesterIdOrderByCreatedDesc(anyLong(), any());
        }

        @Test
        void getItemRequestById() {
            itemRequestService.getItemRequestById(1L, 1L);
            verify(itemRequestStorage, times(1)).getReferenceById(anyLong());
        }

        @Test
        void getAllRequests() {
            itemRequestService.getAllItemRequests(1L, 1, 1);
            verify(itemRequestStorage, times(1)).getAll(PageRequest
                    .of(1, 1, Sort.by("created").descending()));
        }
    }
}
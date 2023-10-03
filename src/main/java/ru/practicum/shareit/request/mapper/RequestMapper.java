package ru.practicum.shareit.request.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestMapper {

    private final ItemStorage itemStorage;

    private final ItemMapper itemMapper;

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requesterId(itemRequest.getRequesterId())
                .created(itemRequest.getCreated())
                .build();
    }

    public void addItemsToRequest(ItemRequestDto requestDto) {
        List<ItemDto> actualItems = itemMapper.toItemDtoList(itemStorage
                .findAllByRequestIdOrderByIdDesc(requestDto.getId()));
        requestDto.setItems(Objects.requireNonNullElseGet(actualItems, List::of));
    }

    public ItemRequest fromItemRequestDto(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requesterId(itemRequestDto.getRequesterId())
                .created(itemRequestDto.getCreated())
                .build();
    }

    public List<ItemRequestDto> fromListToItemRequestList(List<ItemRequest> list) {
        return list.stream().map(this::toItemRequestDto).collect(Collectors.toList());
    }

    public void updateRequesterIdAndCreationDate(Long requesterId, ItemRequest itemRequest) {
        itemRequest.setRequesterId(requesterId);
        itemRequest.setCreated(LocalDateTime.now());
    }
}

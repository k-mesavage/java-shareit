package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.utility.ObjectChecker;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final RequestMapper requestMapper;
    private final ObjectChecker objectChecker;
    private final ItemRequestStorage itemRequestStorage;

    @Override
    public ItemRequestDto addItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        objectChecker.userFound(userId);
        ItemRequest newItemRequest = requestMapper.fromItemRequestDto(itemRequestDto);
         requestMapper.updateRequesterIdAndCreationDate(userId, newItemRequest);
        return requestMapper.toItemRequestDto(itemRequestStorage.save(newItemRequest));
    }

    @Override
    public List<ItemRequestDto> getAllItemRequestsByUser(Long userId) {
        objectChecker.userFound(userId);
        List<ItemRequest> itemRequestList = itemRequestStorage.findAllByRequesterIdOrderByCreatedDesc(userId, Pageable.unpaged());
        List<ItemRequestDto> itemRequestDtoList = requestMapper.fromListToItemRequestList(itemRequestList);
        itemRequestDtoList.forEach(requestMapper::addItemsToRequest);
        return itemRequestDtoList;
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, int from, int size) {
        List<ItemRequestDto> allItemRequests = itemRequestStorage.getAll(PageRequest
                .of(from, size, Sort.by("created").descending())).stream()
                        .map(requestMapper::toItemRequestDto)
                                .filter(i -> !i.getRequesterId().equals(userId))
                                        .collect(Collectors.toList());
        allItemRequests.forEach(requestMapper::addItemsToRequest);
        return allItemRequests;
    }

    @Override
    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {
        objectChecker.userFound(userId);
        ItemRequestDto itemRequestDto = requestMapper.toItemRequestDto(itemRequestStorage.getReferenceById(requestId));
        requestMapper.addItemsToRequest(itemRequestDto);
        return itemRequestDto;
    }
}

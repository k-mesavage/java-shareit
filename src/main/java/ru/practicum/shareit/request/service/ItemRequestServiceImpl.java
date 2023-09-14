package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.utility.ObjectChecker;

import java.util.List;

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
        List<ItemRequest> itemRequestList = itemRequestStorage.findAllByRequesterIdOrderByCreationDateDesc(userId);
        return requestMapper.fromListToItemRequestList(itemRequestList);
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests() {
        List<ItemRequest> allItemRequests = itemRequestStorage.findAll();
        return requestMapper.fromListToItemRequestList(allItemRequests);
    }

    @Override
    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {
        objectChecker.userFound(userId);
        ItemRequestDto itemRequestDto = requestMapper.toItemRequestDto(itemRequestStorage.getReferenceById(requestId));
        requestMapper.addItemsToRequest(itemRequestDto);
        return itemRequestDto;
    }
}

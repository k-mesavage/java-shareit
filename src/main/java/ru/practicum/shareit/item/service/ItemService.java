package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getAllItemsByUserId(Long userId);

    void deleteItem(Long userId, Long itemId);

    List<ItemDto> searchItems(String text);
}

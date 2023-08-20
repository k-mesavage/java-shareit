package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item addItem(Long userId, ItemDto itemDto);

    Item updateItem(Long itemId, Long userId, String params);

    Item getItemById(Long itemId);

    List<Item> getAllItemsByUserId(Long userId);

    void deleteItem(Long userId, Long itemId);

    List<Item> searchItems(String text);
}

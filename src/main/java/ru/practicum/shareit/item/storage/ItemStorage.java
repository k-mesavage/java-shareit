package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemStorage {

    Item addItem(ItemDto item, User owner);

    List<Item> getItemsByUserId(Long userId);

    Item getItemById(Long itemId);

    Item updateItem(Long itemId, Long userId, String params);

    List<Item> searchItems(String text);

    void deleteItem(Long userId, Long itemId);
}

package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage storage;
    private final UserStorage userStorage;

    @Override
    public Item addItem(Long userId, Item item) {
        User user = userStorage.getUserById(userId);
        if(user == null) {
            throw new ObjectNotFoundException("User");
        }
        return storage.addItem(item, user);
    }

    @Override
    public Item updateItem(Long itemId, Long userId, ItemDto itemDto) {
        return storage.updateItem(itemId, userId, itemDto);
    }

    @Override
    public Item getItemById(Long itemId) {
        return storage.getItemById(itemId);
    }

    @Override
    public List<Item> getAllItemsByUserId(Long userId) {
        return storage.getItemsByUserId(userId);
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        storage.deleteItem(userId, itemId);
    }

    @Override
    public List<Item> searchItems(String text) {
        return storage.searchItems(text);
    }
}

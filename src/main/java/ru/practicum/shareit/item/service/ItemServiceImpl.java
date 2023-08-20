package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemStorage storage;
    private final UserStorage userStorage;

    @Override
    public Item addItem(Long userId, ItemDto itemDto) {
        if (!itemDto.isAvailable()) {
            throw new BadRequestException("Available Exception");
        }
        User owner = userStorage.getUserById(userId);
        if (owner == null) {
            throw new ObjectNotFoundException("User Not Found Exception");
        }
        return storage.addItem(itemDto, userStorage.getUserById(userId));
    }

    @Override
    public Item updateItem(Long itemId, Long userId, String params) {
        return storage.updateItem(itemId, userId, params);
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

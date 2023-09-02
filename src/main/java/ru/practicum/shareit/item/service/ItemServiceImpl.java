package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage storage;
    private final UserStorage userStorage;

    @Override
    @Transactional
    public Item addItem(Long userId, Item item) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User Not Found"));
        item.setOwner(user);
        return storage.save(item);
    }

    @Override
    @Transactional
    public Item updateItem(Long itemId, Long userId, ItemDto itemDto) {
        Item updatedItem = storage.getReferenceById(itemId);
        if (Objects.equals(updatedItem.getOwner().getId(), userId)) {
            if (updatedItem.getId().equals(itemId)) {
                if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
                    updatedItem.setName(itemDto.getName());
                }
                if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
                    updatedItem.setDescription(itemDto.getDescription());
                }
                if (itemDto.getAvailable() != null) {
                    updatedItem.setAvailable(itemDto.getAvailable());
                }
                storage.save(updatedItem);
                return updatedItem;
            }
        } else throw new ObjectNotFoundException("Updated Item");
        return null;
    }

    @Override
    @Transactional
    public Item getItemById(Long itemId) {
        return storage.getReferenceById(itemId);
    }

    @Override
    @Transactional
    public List<Item> getAllItemsByUserId(Long userId) {
        return storage.findAllByOwnerId(userId);
    }

    @Override
    @Transactional
    public void deleteItem(Long userId, Long itemId) {
        storage.deleteById(itemId);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return storage.searchAvailableItems(text.toLowerCase());
    }
}

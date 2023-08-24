package ru.practicum.shareit.item.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@AllArgsConstructor
public class ItemStorageImpl implements ItemStorage {

    private static final Map<Long, Map<Long, Item>> itemsMap = new HashMap<>();
    private static final Map<Long, Item> allItems = new HashMap<>();
    private static Long id = 0L;

    @Override
    public Item addItem(Item item, User owner) {
        item.setId(generateId());
        item.setOwner(owner);
        Map<Long, Item> newItemMap = new HashMap<>();
        newItemMap.put(item.getId(), item);
        Map<Long, Item> userItems = itemsMap.get(owner.getId());
        if (userItems != null) {
            for (Item i : userItems.values()) {
                newItemMap.put(i.getId(), i);
            }
        }
        itemsMap.put(owner.getId(), newItemMap);
        allItems.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getItemsByUserId(Long userId) {
        return new ArrayList<>(itemsMap.get(userId).values());
    }

    @Override
    public Item getItemById(Long itemId) {
        return allItems.get(itemId);
    }

    @Override
    public Item updateItem(Long itemId, Long userId, ItemDto itemDto) {
        Map<Long, Item> userItems = itemsMap.get(userId);
        if (userItems == null) {
            throw new ObjectNotFoundException("Items");
        }
        Item updatedItem = userItems.get(itemId);
        if (updatedItem == null) {
            throw new ObjectNotFoundException("Item");
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            updatedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            updatedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updatedItem.setAvailable(itemDto.getAvailable());
        }
        userItems.put(updatedItem.getId(), updatedItem);
        itemsMap.put(userId, userItems);
        allItems.put(updatedItem.getId(), updatedItem);
        return updatedItem;
    }

    @Override
    public List<Item> searchItems(String text) {
        List<Item> resultOfSearch = new ArrayList<>();
        if (!text.isEmpty()) {
            for (Item item : allItems.values()) {
                if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    if (item.isAvailable()) {
                        resultOfSearch.add(item);
                    }
                }
            }
        }
        return resultOfSearch;
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        Map<Long, Item> userItems = itemsMap.get(userId);
        if (userItems.get(itemId) == null) {
            throw new ObjectNotFoundException("Item");
        }
        userItems.remove(itemId);
        itemsMap.put(userId, userItems);
        allItems.remove(itemId);
    }

    private Long generateId() {
        return ++id;
    }
}

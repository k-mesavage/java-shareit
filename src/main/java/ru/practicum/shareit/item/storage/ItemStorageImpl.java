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

    private final Map<Long, Map<Long, Item>> itemsMap = new HashMap<>();
    private final Map<Long, Item> allItems = new HashMap<>();
    private static Long id = 0L;

    @Override
    public Item addItem(Item item, User owner) {
        item.setId(generateId());
        item.setOwner(owner);
        final Map<Long, Item> items = itemsMap.computeIfAbsent(owner.getId(), k -> new HashMap<>());
        items.put(item.getId(), item);
        allItems.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getItemsByUserId(Long userId) {
        return new ArrayList<>(itemsMap.getOrDefault(userId, new HashMap<>()).values());
    }

    @Override
    public Item getItemById(Long itemId) {
        return Optional.of(allItems.get(itemId)).orElseThrow(() -> new ObjectNotFoundException("Item"));
    }

    @Override
    public Item updateItem(Long itemId, Long userId, ItemDto itemDto) {
        final Map<Long, Item> userItems = itemsMap.get(userId);
        if (userItems == null) {
            throw new ObjectNotFoundException("Items");
        }
        final Item updatedItem = userItems.get(itemId);
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
        return updatedItem;
    }

    @Override
    public List<Item> searchItems(String text) {
        final List<Item> resultOfSearch = new ArrayList<>();
        if (!text.isBlank()) {
            for (Item item : allItems.values()) {
                if (item.getName().toLowerCase().contains(text) ||
                        item.getDescription().toLowerCase().contains(text)) {
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
        itemsMap.getOrDefault(userId, new HashMap<>()).remove(itemId);
        allItems.remove(itemId);
    }

    private Long generateId() {
        return ++id;
    }
}

package ru.practicum.shareit.item.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.UpdateItemBuilder;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@AllArgsConstructor
public class ItemStorageImpl implements ItemStorage {

    private static Map<Long, Map<Long, Item>> itemsMap = new HashMap<>();
    private static Map<Long, Item> allItems = new HashMap<>();
    private static Long id = 0L;

    private final ItemMapper mapper;

    @Override
    public Item addItem(ItemDto itemDto, User owner) {
        Item newItem = mapper.fromItemDto(itemDto, owner);
        newItem.setId(generateId());
        Map<Long, Item> newItemMap = new HashMap<>();
        newItemMap.put(newItem.getId(), newItem);
        Map<Long, Item> userItems = itemsMap.get(owner.getId());
        if (userItems != null) {
            for (Item item : userItems.values()) {
                newItemMap.put(item.getId(), item);
            }
        }
        itemsMap.put(owner.getId(), newItemMap);
        allItems.put(newItem.getId(), newItem);
        return newItem;
    }

    @Override
    public List<Item> getItemsByUserId(Long userId) {
        List<Item> userItems = new ArrayList<>(itemsMap.get(userId).values());
        if (!userItems.isEmpty()) {
            return userItems;
        }
        return new ArrayList<>();
    }

    @Override
    public Item getItemById(Long itemId) {
        return allItems.get(itemId);
    }

    @Override
    public Item updateItem(Long itemId, Long userId, String params) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        UpdateItemBuilder updateItemBuilder;
        try {
            updateItemBuilder = objectMapper.readValue(params, UpdateItemBuilder.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON Invalid Format", e);
        }
        Map<Long, Item> userItems = itemsMap.get(userId);
        if (userItems == null) {
            throw new ObjectNotFoundException("Items Not Found Exception");
        }
        Item updatedItem = userItems.get(itemId);
        if (updatedItem == null) {
            throw new ObjectNotFoundException("Update Item Exception");
        }
        if (updateItemBuilder.name != null) {
            updatedItem.setName(updateItemBuilder.name);
        }
        if (updateItemBuilder.description != null) {
            updatedItem.setDescription(updateItemBuilder.description);
        }
        if (updateItemBuilder.available != null) {
            if (updateItemBuilder.available.equals("false")) {
                updatedItem.setAvailable(false);
            } else {
                updatedItem.setAvailable(true);
            }
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
            throw new ObjectNotFoundException("Item Not Found Exception");
        }
        userItems.remove(itemId);
        itemsMap.put(userId, userItems);
        allItems.remove(itemId);
    }

    private Long generateId() {
        return ++id;
    }
}

package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest());
    }

    public Item fromItemDto(ItemDto itemDto, User owner) {
        return new Item(0L,
                itemDto.getName(),
                itemDto.getDescription(),
                owner,
                itemDto.isAvailable(),
                itemDto.getRequest());
    }
}

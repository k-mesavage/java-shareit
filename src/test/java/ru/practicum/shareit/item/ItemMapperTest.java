package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @InjectMocks
    ItemMapper itemMapper;

    private final Item actualItem = Item.builder()
            .id(1L)
            .name("Item")
            .description("Desc")
            .requestId(1L)
            .available(true)
            .build();
    private final ItemDto actualItemDto = ItemDto.builder()
            .id(1L)
            .name("Item")
            .description("Desc")
            .requestId(1L)
            .available(true)
            .build();

    @Test
    void toItemDto() {
        ItemDto expectedItemDto = itemMapper.toItemDto(actualItem);
        assertEquals(expectedItemDto.getId(), actualItem.getId());
        assertEquals(expectedItemDto.getName(), actualItem.getName());
    }

    @Test
    void fromItemDto() {
        Item expectedItem = itemMapper.fromItemDto(actualItemDto);
        assertEquals(expectedItem.getId(), actualItemDto.getId());
        assertEquals(expectedItem.getDescription(), actualItemDto.getDescription());
    }

    @Test
    void toBooking() {
        ShortItemDto expectedBookingItem = itemMapper.toBooking(actualItem);
        assertEquals(expectedBookingItem.getId(), actualItem.getId());
        assertEquals(expectedBookingItem.getName(), actualItem.getName());
    }

    @Test
    void updateItem() {
        Item expectedItem = itemMapper.updateItem(actualItem, actualItemDto);
        assertEquals(expectedItem.getDescription(), actualItemDto.getDescription());
        assertEquals(expectedItem.getName(), actualItemDto.getName());
    }

    @Test
    void toItemDtoList() {
        List<ItemDto> expectedList = itemMapper.toItemDtoList(List.of(actualItem));
        assertEquals(expectedList.get(0).getId(), actualItem.getId());
        assertEquals(expectedList.get(0).getName(), actualItem.getName());
    }
}
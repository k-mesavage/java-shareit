package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.ItemRequest;

import javax.validation.constraints.NotEmpty;

@Data
public class ItemDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    private boolean available;

    private ItemRequest request;

    public ItemDto(String name, String description, boolean available, ItemRequest request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}


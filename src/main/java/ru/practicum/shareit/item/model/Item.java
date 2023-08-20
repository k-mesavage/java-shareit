package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
public class Item {

    private Long id;

    private String name;

    private String description;

    private User owner;

    private boolean available;

    private ItemRequest request;

    public Item(Long id, String name, String description, User owner, boolean available, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.available = available;
        this.request = request;
    }
}

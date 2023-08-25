package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@Data
public class ItemRequest {

    private Long id;

    private String description;

    private User requestor;

    private LocalDate created;

    public ItemRequest(Long id, String description, User requestor, LocalDate created) {
        this.id = id;
        this.description = description;
        this.requestor = requestor;
        this.created = created;
    }
}

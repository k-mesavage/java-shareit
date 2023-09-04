package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {

    public Long id;

    private String description;

    private String authorName;
}

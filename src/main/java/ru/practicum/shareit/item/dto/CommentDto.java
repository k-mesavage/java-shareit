package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.CreateConstraint;
import ru.practicum.shareit.validation.UpdateConstraint;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder

public class CommentDto {

    public Long id;

    @NotBlank(groups = {CreateConstraint.class, UpdateConstraint.class})
    private String text;

    private String authorName;

    private LocalDateTime created;
}

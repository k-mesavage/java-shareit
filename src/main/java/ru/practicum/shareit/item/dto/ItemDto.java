package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.CreateConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemDto {

    private Long id;

    @NotBlank(groups = CreateConstraint.class)
    private String name;

    @NotBlank(groups = CreateConstraint.class)
    private String description;

    @NotNull(groups = CreateConstraint.class)
    private Boolean available;
}


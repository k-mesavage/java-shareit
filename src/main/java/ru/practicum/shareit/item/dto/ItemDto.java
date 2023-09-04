package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.validation.CreateConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemDto {

    public Long id;

    @NotBlank(groups = CreateConstraint.class)
    public String name;

    @NotBlank(groups = CreateConstraint.class)
    public String description;

    @NotNull(groups = CreateConstraint.class)
    public Boolean available;

    public ShortBookingDto lastBooking;

    public ShortBookingDto nextBooking;
}


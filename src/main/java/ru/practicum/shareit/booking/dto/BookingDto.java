package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ShortBookingItem;
import ru.practicum.shareit.user.dto.ShortUserDto;
import ru.practicum.shareit.validation.CreateConstraint;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {

    public Long id;

    @NotBlank(groups = CreateConstraint.class)
    public LocalDateTime start;

    @NotBlank(groups = CreateConstraint.class)
    public LocalDateTime end;

    public String status;

    public ShortUserDto booker;

    public ShortBookingItem item;
}

package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.BookingItem;
import ru.practicum.shareit.user.dto.BookingUser;
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

    public BookingUser booker;

    public BookingItem item;
}

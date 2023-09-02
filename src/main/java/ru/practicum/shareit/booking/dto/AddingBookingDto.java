package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.CreateConstraint;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class AddingBookingDto {

    @NotBlank(groups = CreateConstraint.class)
    public LocalDateTime start;

    @NotBlank(groups = CreateConstraint.class)
    public LocalDateTime end;

    @NotBlank(groups = CreateConstraint.class)
    public Long itemId;
}

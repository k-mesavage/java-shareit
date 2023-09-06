package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.CreatedDate;
import ru.practicum.shareit.validation.CreateConstraint;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder
public class WorkingBookingDto {

    @NotNull(groups = CreateConstraint.class)
    @CreatedDate
    LocalDateTime start;

    @NotNull(groups = CreateConstraint.class)
    LocalDateTime end;

    @NotNull(groups = CreateConstraint.class)
    Long itemId;
}

package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import ru.practicum.shareit.validation.CreateConstraint;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class WorkingBookingDto {

    @NotNull(groups = CreateConstraint.class)
    @CreatedDate
    public LocalDateTime start;

    @NotNull(groups = CreateConstraint.class)
    public LocalDateTime end;

    @NotNull(groups = CreateConstraint.class)
    public Long itemId;
}

package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.user.dto.ShortUserDto;
import ru.practicum.shareit.validation.CreateConstraint;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {

    private Long id;

    @NotBlank(groups = CreateConstraint.class)
    private LocalDateTime start;

    @JsonFormat
    @NotBlank(groups = CreateConstraint.class)
    private LocalDateTime end;

    private String status;

    private ShortUserDto booker;

    private ShortItemDto item;
}

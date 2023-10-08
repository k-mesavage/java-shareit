package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class WorkingBookingDto {

    LocalDateTime start;

    LocalDateTime end;

    Long itemId;
}

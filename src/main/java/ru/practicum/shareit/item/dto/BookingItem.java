package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingItem {

    public Long id;

    public String name;

}

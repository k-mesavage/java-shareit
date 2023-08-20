package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
public class Booking {

    private Long id;

    private LocalDate start;

    private LocalDate end;

    private Item item;

    private User booker;

    private String status;

    public Booking(Long id, LocalDate start, LocalDate end, Item item, User booker, String status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }
}

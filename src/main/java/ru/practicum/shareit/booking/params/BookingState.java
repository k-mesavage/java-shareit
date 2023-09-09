package ru.practicum.shareit.booking.params;

import ru.practicum.shareit.exception.UnsupportedStatusException;

public enum BookingState {
    WAITING,
    APPROVED,
    REJECTED,
    ALL,
    CURRENT,
    PAST,
    FUTURE;
    public static BookingState getValue(String state) {
        try {
            return BookingState.valueOf(state);
        } catch (Throwable e) {
            throw new UnsupportedStatusException(state);
        }
    }
}

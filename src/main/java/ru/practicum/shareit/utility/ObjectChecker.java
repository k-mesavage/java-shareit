package ru.practicum.shareit.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectChecker {

    private final BookingStorage bookingStorage;

    private final UserStorage userStorage;

    private final ItemStorage itemStorage;

    public void user(Long userId) {
        if (!userStorage.existsById(userId)) {
            throw new ObjectNotFoundException("User Not Found");
        }
    }

    public void booker(Long ownerId, Long bookerId) {
        if (ownerId.equals(bookerId)) {
            throw new ObjectNotFoundException("Owner Book Exception");
        }
    }

    public void item(Long itemId) {
        if (!itemStorage.existsById(itemId)) {
            throw new ObjectNotFoundException("Item Not Found");
        }
    }

    public void itemAvailable(Long itemId) {
        if (!itemStorage.getReferenceById(itemId).getAvailable()) {
            throw new BadRequestException("Item Available Exception");
        }
    }

    public void checkBookingDate(ShortBookingDto shortBookingDto, Long itemId) {
        List<Booking> checkingBookings = bookingStorage.findAllByItemId(itemId);
        for (Booking booking : checkingBookings) {
            if (shortBookingDto.getStart().isBefore(booking.getEnd()) &&
                    booking.getStart().isBefore(shortBookingDto.getEnd())) {
                throw new BadRequestException("Available Item Exception");
            }
            if (shortBookingDto.getStart().equals(shortBookingDto.getEnd())) {
                throw new BadRequestException("Available Item Exception");
            }
            if (shortBookingDto.getEnd().isBefore(shortBookingDto.getStart())) {
                throw new BadRequestException("Available Item Exception");
            }
        }
    }

    public void checkDateTime(ShortBookingDto shortBookingDto) {
        if (shortBookingDto.getStart().isBefore(LocalDateTime.now()) ||
                shortBookingDto.getEnd().isBefore(LocalDateTime.now()) ||
                shortBookingDto.getEnd().isBefore(shortBookingDto.getStart()) ||
                shortBookingDto.getEnd().equals(shortBookingDto.getStart())) {
            throw new BadRequestException("Check Date Exception");
        }
    }
}

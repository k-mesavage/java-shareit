package ru.practicum.shareit.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.WorkingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.booking.params.BookingState.APPROVED;

@Service
@Transactional(noRollbackFor = Throwable.class)
@RequiredArgsConstructor
public class ObjectChecker {

    private final BookingStorage bookingStorage;

    private final UserStorage userStorage;

    private final ItemStorage itemStorage;

    public void userFound(Long userId) {
        if (!userStorage.existsById(userId)) {
            throw new ObjectNotFoundException("User Not Found");
        }
    }

    public void userAccess(Long userId, Long ownerId) {
        if (!Objects.equals(userId, ownerId)) {
            throw new ObjectNotFoundException("User Access Exception");
        }
    }

    public void reApprove(Booking booking) {
        if (booking.getStatus().equals(APPROVED.toString())) {
            throw new BadRequestException("ReApproved Exception");
        }
    }

    public void bookerAccess(Long ownerId, Long bookerId) {
        if (ownerId.equals(bookerId)) {
            throw new ObjectNotFoundException("Owner Book Exception");
        }
    }

    public void itemFound(Long itemId) {
        if (!itemStorage.existsById(itemId)) {
            throw new ObjectNotFoundException("Item Not Found");
        }
    }

    public void itemAvailable(Long itemId) {
        if (!itemStorage.getReferenceById(itemId).getAvailable()) {
            throw new BadRequestException("Item Available Exception");
        }
    }

    public void checkBookingDate(WorkingBookingDto workingBookingDto, Long itemId) {
        List<Booking> checkingBookings = bookingStorage.findAllByItemId(itemId);
        for (Booking booking : checkingBookings) {
            if (workingBookingDto.getStart().isBefore(booking.getEnd()) &&
                    booking.getStart().isBefore(workingBookingDto.getEnd())) {
                throw new BadRequestException("Available Item Exception");
            }
            if (workingBookingDto.getStart().equals(workingBookingDto.getEnd())) {
                throw new BadRequestException("Available Item Exception");
            }
            if (workingBookingDto.getEnd().isBefore(workingBookingDto.getStart())) {
                throw new BadRequestException("Available Item Exception");
            }
        }
    }

    public void checkDateTime(WorkingBookingDto workingBookingDto) {
        if (workingBookingDto.getStart().isBefore(LocalDateTime.now()) ||
                workingBookingDto.getEnd().isBefore(LocalDateTime.now()) ||
                workingBookingDto.getEnd().isBefore(workingBookingDto.getStart()) ||
                workingBookingDto.getEnd().equals(workingBookingDto.getStart())) {
            throw new BadRequestException("Check Date Exception");
        }
    }

    public void bookingFound(Long userId, Long itemId) {
        if (bookingStorage.findFirstByItemIdAndBookerIdAndStatusAndEndBefore(itemId,
                userId,
                "APPROVED",
                LocalDateTime.now()) == null) {
            throw new BadRequestException("Booking Found Exception");
        }
    }
}

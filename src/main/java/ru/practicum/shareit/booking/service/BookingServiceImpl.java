package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.WorkingBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.params.BookingState;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.utility.ObjectChecker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.booking.params.BookingState.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserStorage userStorage;

    private final ItemStorage itemStorage;

    private final BookingStorage bookingStorage;

    private final BookingMapper bookingMapper;

    private final ObjectChecker objectChecker;

    @Override
    @Transactional
    public BookingDto addBooking(Long bookerId, WorkingBookingDto workingBookingDto) {
        objectChecker.checkDateTime(workingBookingDto);
        objectChecker.itemFound(workingBookingDto.getItemId());
        objectChecker.userFound(bookerId);
        Item item = itemStorage.getReferenceById(workingBookingDto.getItemId());
        objectChecker.itemAvailable(item.getId());
        objectChecker.checkBookingDate(workingBookingDto, item.getId());
        objectChecker.bookerAccess(item.getOwner().getId(), bookerId);
        Booking newBooking = Booking.builder()
                .start(workingBookingDto.getStart())
                .end(workingBookingDto.getEnd())
                .status(BookingState.WAITING.toString())
                .itemId(workingBookingDto.getItemId())
                .bookerId(bookerId)
                .build();
        newBooking = bookingStorage.save(newBooking);
        return bookingMapper.toDto(newBooking);
    }

    @Override
    public BookingDto requestBooking(Boolean approved, Long bookingId, Long userId) {
        Booking booking = bookingStorage.getReferenceById(bookingId);
        Item item = itemStorage.getReferenceById(booking.getItemId());
        User owner = userStorage.getReferenceById(item.getOwner().getId());
        objectChecker.userAccess(userId, owner.getId());
        if (approved) {
            objectChecker.reApprove(booking);
            booking.setStatus(APPROVED.toString());
        } else {
            booking.setStatus(REJECTED.toString());
        }
        bookingStorage.save(booking);

        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsByUser(Long bookerId, String state) {
        objectChecker.userFound(bookerId);
        BookingState status = BookingState.getValue(state);
        List<Booking> bookings = new ArrayList<>();
        if (status.equals(CURRENT)) {
            bookings = bookingStorage.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId,
                    LocalDateTime.now(),
                    LocalDateTime.now());
        }
        if (status.equals(ALL)) {
            bookings = bookingStorage.findAllByBookerIdOrderByStartDesc(bookerId);
        }
        if (status.equals(PAST)) {
            bookings = bookingStorage.findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
        }
        if (status.equals(FUTURE)) {
            bookings = bookingStorage.findAllByBookerIdAndStatusFuture(bookerId, LocalDateTime.now());
        }
        if (status.equals(WAITING)) {
            bookings = bookingStorage.findAllByBookerIdAndStatus(bookerId, state);
        }
        if (status.equals(REJECTED)) {
            bookings = bookingStorage.findAllByBookerIdAndStatus(bookerId, state);
        }
        return bookingMapper.fromListToDtoList(bookings);
    }

    public List<BookingDto> getAllItemsBookingByOwner(Long ownerId, String state) {
        objectChecker.userFound(ownerId);
        List<Booking> bookings = new ArrayList<>();
        BookingState status = BookingState.getValue(state);
        if (status.equals(CURRENT)) {
            bookings = bookingStorage
                    .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId,
                            LocalDateTime.now(),
                            LocalDateTime.now());
        }
        if (status.equals(ALL)) {
            bookings = bookingStorage.findAllByItemOwnerIdOrderByStartDesc(ownerId);
        }
        if (status.equals(PAST)) {
            bookings = bookingStorage.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
        }
        if (status.equals(FUTURE)) {
            bookings = bookingStorage.findAllByItemOwnerIdAndStatusFuture(ownerId, LocalDateTime.now());
        }
        if (status.equals(WAITING)) {
            bookings = bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, state);
        }
        if (status.equals(REJECTED)) {
            bookings = bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, state);
        }
        return bookingMapper.fromListToDtoList(bookings);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingStorage.getReferenceById(bookingId);
        Item item = itemStorage.getReferenceById(booking.getItemId());
        try {
            objectChecker.userAccess(booking.getBookerId(), userId);
        } catch (ObjectNotFoundException e) {
            objectChecker.userAccess(item.getOwner().getId(), userId);
        }
        return bookingMapper.toDto(booking);
    }

    private class HandleGetter {

        private List<Booking> bookings = new ArrayList<>();

        private List<Booking> HandleRequest (BookingState status, Long senderId, String sender) {
            if (status.equals(CURRENT)) {
                getCurrent(senderId, sender);
            }
            if (status.equals(ALL)) {
                getAll(senderId, sender);
            }
            if (status.equals(PAST)) {
                getPast(senderId, sender);
            }
            if (status.equals(FUTURE)) {
                getFuture(senderId, sender);
            }
            if (status.equals(WAITING)) {
                getWaiting(senderId, sender);
            }
            if (status.equals(REJECTED)) {
                getRejected(senderId, sender);
            }
            return bookings;
        }

        private void getCurrent(Long senderId, String sender) {
            if (sender.equals("user")) {
                bookings = bookingStorage.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(senderId,
                        LocalDateTime.now(),
                        LocalDateTime.now());
            } else if (sender.equals("owner")) {
                bookings = bookingStorage
                        .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(senderId,
                                LocalDateTime.now(),
                                LocalDateTime.now());
            }
        }

        private void getAll(Long senderId, String sender) {
            if (sender.equals("user")) {
                bookings = bookingStorage.findAllByBookerIdOrderByStartDesc(senderId);
            } else if (sender.equals("owner")) {
                bookings = bookingStorage.findAllByItemOwnerIdOrderByStartDesc(senderId);
            }
        }

        private void getPast(Long senderId, String sender) {
            if (sender.equals("user")) {
                bookings = bookingStorage.findAllByBookerIdAndEndBeforeOrderByStartDesc(senderId, LocalDateTime.now());
            } else if (sender.equals("owner")) {
                bookings = bookingStorage.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(senderId, LocalDateTime.now());
            }
        }

        private void getFuture(Long senderId, String sender) {
            if (sender.equals("user")) {
                bookings = bookingStorage.findAllByBookerIdAndStatusFuture(senderId, LocalDateTime.now());
            } else if (sender.equals("owner")) {
                bookings = bookingStorage.findAllByItemOwnerIdAndStatusFuture(senderId, LocalDateTime.now());
            }
        }

        private void getWaiting(Long senderId, String sender) {
            if (sender.equals("user")) {
                bookings = bookingStorage.findAllByBookerIdAndStatus(senderId, "WAITING");
            } else if (sender.equals("owner")) {
                bookings = bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(senderId, "WAITING");
            }
        }

        private void getRejected(Long senderId, String sender) {
            if (sender.equals("user")) {
                bookings = bookingStorage.findAllByBookerIdAndStatus(senderId, "REJECTED");
            } else if (sender.equals("owner")) {
                bookings = bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(senderId, "REJECTED");
            }
        }
    }
}


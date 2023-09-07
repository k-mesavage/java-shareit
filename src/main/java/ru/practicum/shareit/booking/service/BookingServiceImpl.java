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

    HandleGetterForUser getterForUser = new GetRejected(new GetWaiting(new GetFuture(new GetPast(new GetAll(new GetCurrent(null))))));

    HandleGetterForOwner getterForOwner = new GetRejectedForOwner(new GetWaitingForOwner(new GetFutureForOwner(new GetPastForOwner(new GetAllForOwner(new GetCurrentForOwner(null))))));

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
        BookingState status = BookingState.getValue(state);

        List<Booking> bookings = getterForOwner.handleRequest(status, ownerId);
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

    abstract class HandleGetterForUser {

        HandleGetterForUser handleGetterForUser;

        List<Booking> bookings;

        public HandleGetterForUser(HandleGetterForUser handleGetterForUser) {
            this.handleGetterForUser = handleGetterForUser;
        }

        abstract List<Booking> handleRequest(BookingState status, Long senderId, String sender);
    }

    class GetCurrent extends HandleGetterForUser {

        public GetCurrent(HandleGetterForUser handleGetterForUser) {
            super(handleGetterForUser);
        }

        @Override
        List<Booking> handleRequest(BookingState status, Long senderId, String sender) {
            if (!status.equals(CURRENT)) {
                handleGetterForUser.handleRequest(status, senderId, sender);
            }
            this.bookings = bookingStorage.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(senderId,
                    LocalDateTime.now(),
                    LocalDateTime.now());
            return bookings;
        }
    }

    class GetAll extends HandleGetterForUser {

        public GetAll(HandleGetterForUser handleGetterForUser) {
            super(handleGetterForUser);
        }

        @Override
        List<Booking> handleRequest(BookingState status, Long senderId, String sender) {
            if (!status.equals(ALL)) {
                handleGetterForUser.handleRequest(status, senderId, sender);
            }
            this.bookings = bookingStorage.findAllByBookerIdOrderByStartDesc(senderId);
            return bookings;
        }
    }

    class GetPast extends HandleGetterForUser {

        public GetPast(HandleGetterForUser handleGetterForUser) {
            super(handleGetterForUser);
        }

        @Override
        List<Booking> handleRequest(BookingState status, Long senderId, String sender) {
            if (!status.equals(PAST)) {
                handleGetterForUser.handleRequest(status, senderId, sender);
            }
            this.bookings = bookingStorage.findAllByBookerIdAndEndBeforeOrderByStartDesc(senderId, LocalDateTime.now());
            return bookings;
        }
    }

    class GetFuture extends HandleGetterForUser {

        public GetFuture(HandleGetterForUser handleGetterForUser) {
            super(handleGetterForUser);
        }

        @Override
        List<Booking> handleRequest(BookingState status, Long senderId, String sender) {
            if (!status.equals(FUTURE)) {
                handleGetterForUser.handleRequest(status, senderId, sender);
            }
            this.bookings = bookingStorage.findAllByBookerIdAndStatusFuture(senderId, LocalDateTime.now());
            return bookings;
        }
    }

    class GetWaiting extends HandleGetterForUser {

        public GetWaiting(HandleGetterForUser handleGetterForUser) {
            super(handleGetterForUser);
        }

        @Override
        List<Booking> handleRequest(BookingState status, Long senderId, String sender) {
            if (!status.equals(WAITING)) {
                handleGetterForUser.handleRequest(status, senderId, sender);
            }
            this.bookings = bookingStorage.findAllByBookerIdAndStatus(senderId, "WAITING");
            return bookings;
        }
    }

    class GetRejected extends HandleGetterForUser {

        public GetRejected(HandleGetterForUser handleGetterForUser) {
            super(handleGetterForUser);
        }

        @Override
        List<Booking> handleRequest(BookingState status, Long senderId, String sender) {
            this.bookings = bookingStorage.findAllByBookerIdAndStatus(senderId, "REJECTED");
            return bookings;
        }
    }

    abstract class HandleGetterForOwner {

        HandleGetterForOwner handleGetterForOwner;

        List<Booking> bookings;

        public HandleGetterForOwner(HandleGetterForOwner handleGetterForOwner) {
            this.handleGetterForOwner = handleGetterForOwner;
        }

        abstract List<Booking> handleRequest(BookingState status, Long senderId);
    }

    class GetCurrentForOwner extends HandleGetterForOwner {

        public GetCurrentForOwner(HandleGetterForOwner handleGetterForOwner) {
            super(handleGetterForOwner);
        }

        @Override
        List<Booking> handleRequest(BookingState status, Long senderId) {
            if (!status.equals(CURRENT)) {
                handleGetterForOwner.handleRequest(status, senderId);
            }
                this.bookings = bookingStorage
                        .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(senderId,
                                LocalDateTime.now(),
                                LocalDateTime.now());
            return bookings;
        }
    }

    class GetAllForOwner extends HandleGetterForOwner {

        public GetAllForOwner(HandleGetterForOwner handleGetterForOwner) {
            super(handleGetterForOwner);
        }

        @Override
        List<Booking> handleRequest(BookingState status, Long senderId) {
            if (!status.equals(ALL)) {
                handleGetterForOwner.handleRequest(status, senderId);
            }
            this.bookings = bookingStorage.findAllByItemOwnerIdOrderByStartDesc(senderId);
            return bookings;
        }
    }

    class GetPastForOwner extends HandleGetterForOwner {

        public GetPastForOwner(HandleGetterForOwner handleGetterForOwner) {
            super(handleGetterForOwner);
        }

        @Override
        List<Booking> handleRequest(BookingState status, Long senderId) {
            if (!status.equals(PAST)) {
                handleGetterForOwner.handleRequest(status, senderId);
            }
            this.bookings = bookingStorage.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(senderId, LocalDateTime.now());
            return bookings;
        }
    }

    class GetFutureForOwner extends HandleGetterForOwner {

        public GetFutureForOwner(HandleGetterForOwner handleGetterForOwner) {
            super(handleGetterForOwner);
        }

        @Override
        List<Booking> handleRequest(BookingState status, Long senderId) {
            if (!status.equals(FUTURE)) {
                handleGetterForOwner.handleRequest(status, senderId);
            }
            this.bookings = bookingStorage.findAllByItemOwnerIdAndStatusFuture(senderId, LocalDateTime.now());
            return bookings;
        }
    }

    class GetWaitingForOwner extends HandleGetterForOwner {

        public GetWaitingForOwner(HandleGetterForOwner handleGetterForOwner) {
            super(handleGetterForOwner);
        }

        @Override
        List<Booking> handleRequest(BookingState status, Long senderId) {
            if (!status.equals(WAITING)) {
                handleGetterForOwner.handleRequest(status, senderId);
            }
            this.bookings = bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(senderId, "WAITING");
            return bookings;
        }
    }

    class GetRejectedForOwner extends HandleGetterForOwner {

        public GetRejectedForOwner(HandleGetterForOwner handleGetterForOwner) {
            super(handleGetterForOwner);
        }

        @Override
        List<Booking> handleRequest(BookingState status, Long senderId) {
            if (!status.equals(REJECTED)) {
                handleGetterForOwner.handleRequest(status, senderId);
            }
            this.bookings = bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(senderId, "REJECTED");
            return bookings;
        }
    }
}


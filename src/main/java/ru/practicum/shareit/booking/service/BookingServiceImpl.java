package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.WorkingBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.params.BookingState;
import ru.practicum.shareit.booking.params.UserType;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.handler.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.utility.ObjectChecker;

import java.awt.print.Pageable;
import java.util.List;

import static ru.practicum.shareit.booking.params.BookingState.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserStorage userStorage;

    private final ItemStorage itemStorage;

    private final BookingStorage bookingStorage;

    private final BookingMapper bookingMapper;

    private final ObjectChecker objectChecker;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
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
        UserType userType = UserType.USER;
        return getAllBookingsForUserOrOwnerByUserIdAndState(bookerId, state, userType);
    }

    @Override
    public List<BookingDto> getAllItemsBookingByOwner(Long ownerId, String state, int from, int size) {
        objectChecker.userFound(ownerId);
        UserType userType = UserType.OWNER;
        PageRequest pageRequest = PageRequest.of(from, size);
        return getAllBookingsForUserOrOwnerByUserIdAndState(ownerId, state, userType);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
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

    private List<BookingDto> getAllBookingsForUserOrOwnerByUserIdAndState(Long bookerId,
                                                                          String state,
                                                                          UserType userType) {
        final BookingStateHandler handler = BookingStateHandler.link(
                new GetRejected(bookingStorage, bookingMapper),
                new GetWaiting(bookingStorage, bookingMapper),
                new GetFuture(bookingStorage, bookingMapper),
                new GetPast(bookingStorage, bookingMapper),
                new GetCurrent(bookingStorage, bookingMapper),
                new GetAll(bookingStorage, bookingMapper));
        BookingState status = BookingState.getValue(state);
        return handler.handle(bookerId, status, userType);
    }
}
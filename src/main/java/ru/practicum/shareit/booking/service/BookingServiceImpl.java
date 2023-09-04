package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.params.BookingState;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.utility.ObjectChecker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public BookingDto addBooking(Long bookerId, ShortBookingDto shortBookingDto) {
        objectChecker.checkDateTime(shortBookingDto);
        objectChecker.item(shortBookingDto.getItemId());
        objectChecker.user(bookerId);
        Item item = itemStorage.getReferenceById(shortBookingDto.getItemId());
        objectChecker.itemAvailable(item.getId());
        objectChecker.checkBookingDate(shortBookingDto, item.getId());
        objectChecker.booker(item.getOwner().getId(), bookerId);
        Booking newBooking = Booking.builder()
                        .start(shortBookingDto.getStart())
                        .end(shortBookingDto.getEnd())
                        .status(BookingState.WAITING.toString())
                        .itemId(shortBookingDto.getItemId())
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
        if (Objects.equals(userId, owner.getId())) {
            if (approved) {
                if (!booking.getStatus().equals(APPROVED.toString())) {
                    booking.setStatus(APPROVED.toString());
                } else throw new BadRequestException("Approved Exception");
            } else {
                booking.setStatus(REJECTED.toString());
            }
            bookingStorage.save(booking);
            return bookingMapper.toDto(booking);
        } else throw new ObjectNotFoundException("Request Booking Exception");
    }

    @Override
    public List<BookingDto> getAllBookingsByUser(Long bookerId, String state) {
        objectChecker.user(bookerId);
        BookingState status = BookingState.getValue(state);
        List<Booking> bookings = new ArrayList<>();
        if (status.equals(CURRENT)){
            bookings = bookingStorage.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartAsc(bookerId, LocalDateTime.now());
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

    public List<BookingDto> getAllItemsBookingByOwner(Long userId, String state) {
        objectChecker.user(userId);
        List<Booking> bookings = new ArrayList<>();
        BookingState status = BookingState.getValue(state);
        if (status.equals(CURRENT)){
            bookings = bookingStorage.
                    findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartAsc(userId, LocalDateTime.now());
        }
        if (status.equals(ALL)) {
            bookings = bookingStorage.findAllByItemOwnerIdOrderByStartDesc(userId);
        }
        if (status.equals(PAST)) {
            bookings = bookingStorage.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
        }
        if (status.equals(FUTURE)) {
            bookings = bookingStorage.findAllByItemOwnerIdAndStatusFuture(userId, LocalDateTime.now());
        }
        if (status.equals(WAITING)) {
            bookings = bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, state);
        }
        if (status.equals(REJECTED)) {
            bookings = bookingStorage.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, state);
        }
        return bookingMapper.fromListToDtoList(bookings);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingStorage.getReferenceById(bookingId);
        Item item = itemStorage.getReferenceById(booking.getItemId());
        if (booking.getBookerId().equals(userId) || item.getOwner().getId().equals(userId)) {
            return bookingMapper.toDto(booking);
        }
        throw new ObjectNotFoundException("Get Booking Exception");
    }
}


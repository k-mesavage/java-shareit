package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.AddingBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.params.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final UserStorage userStorage;

    private final ItemStorage itemStorage;

    private final BookingStorage bookingStorage;

    private final BookingMapper bookingMapper;

    private final ItemMapper itemMapper;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public BookingDto addBooking(Long bookerId, AddingBookingDto addingBookingDto) {
        Item item = itemStorage.getReferenceById(addingBookingDto.getItemId());
        User booker = userStorage.getReferenceById(bookerId);
        if (item.getId() == null || booker.getId() == null) {
            throw new RuntimeException("Unavailable User");
        }
        if (item.getAvailable()) {
            Booking newBooking = Booking.builder()
                    .start(addingBookingDto.getStart())
                    .end(addingBookingDto.getEnd())
                    .status(BookingStatus.WAITING.toString())
                    .itemId(addingBookingDto.getItemId())
                    .bookerId(bookerId)
                    .build();
            if (checkDate(addingBookingDto, item.getId())) {
                newBooking = bookingStorage.save(newBooking);
                return bookingMapper.toDto(newBooking,
                        itemMapper.toBooking(item),
                        userMapper.toBooking(userStorage.getReferenceById(bookerId)));
            }
        }
        throw new BadRequestException("Add Booking Exception");
    }

    @Override
    public BookingDto requestBooking(Boolean approved, Long bookingId, Long userId) {
        Booking booking = bookingStorage.getReferenceById(bookingId);
        Item item = itemStorage.getReferenceById(booking.getItemId());
        User owner = userStorage.getReferenceById(item.getOwner().getId());
        if (Objects.equals(userId, owner.getId())) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED.toString());
            } else {
                booking.setStatus(BookingStatus.REJECTED.toString());
            }
            bookingStorage.save(booking);
            return bookingMapper.toDto(booking,
                    itemMapper.toBooking(item),
                    userMapper.toBooking(userStorage.getReferenceById(booking.getBookerId())));
        } throw new BadRequestException("Request Booking Exception");
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingStorage.getReferenceById(bookingId);
        Item item = itemStorage.getReferenceById(booking.getItemId());
        if (booking.getBookerId().equals(userId) || item.getOwner().getId().equals(userId)) {
            return bookingMapper.toDto(booking,
                    itemMapper.toBooking(item),
                    userMapper.toBooking(userStorage.getReferenceById(booking.getBookerId())));
        }
        throw new BadRequestException("Get Booking Exception");
    }

    @Override
    public List<Booking> getAllBookings(Long userId, String state) {
        return null;
    }

    @Override
    public List<Booking> getAllFromUser(Long bookerId, String status) {
        List<Booking> bookings = bookingStorage.findAllByBookerIdAndStatus(bookerId, status);
        return null;
    }

    private boolean checkDate(AddingBookingDto addingBookingDto, Long itemId) {
        List<Booking> checkingBookings = bookingStorage.findAllByItemId(itemId);
        for (Booking booking : checkingBookings) {
            if (addingBookingDto.getStart().isBefore(booking.getEnd()) &&
                    booking.getStart().isBefore(addingBookingDto.getEnd())) {
                return false;
            }
            if (addingBookingDto.getStart().equals(addingBookingDto.getEnd())) {
                return false;
            }
            if (addingBookingDto.getEnd().isBefore(addingBookingDto.getStart())) {
                return false;
            }
        }
        return true;
    }
}


package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItemId(Long bookerId);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, String status);
}

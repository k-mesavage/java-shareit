package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.params.BookingState;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItemId(Long itemId);

    @Query("select b from Booking b " +
            "where b.bookerId = (?1) order by b.start desc ")
    List<Booking> findAllByBookerIdOrderByStartDesc(Long booker_id);

    @Query("select b from Booking b " +
            "where b.bookerId = (?1) and b.end > (?2) order by b.start desc")
    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime dateTime);

    @Query("select b from Booking b " +
            "where b.bookerId = (?1) and (b.start > (?2) and b.end < (?2)) order by b.start asc")
    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartAsc(Long userId, LocalDateTime dateTime);

    @Query("select b from Booking b " +
            "where b.bookerId = (?1) and b.start > (?2) order by b.start desc ")
    List<Booking> findAllByBookerIdAndStatusFuture(Long userId, LocalDateTime dateTime);

    @Query("select b from Booking b " +
            "where b.bookerId = (?1) and b.status = (?2) order by b.start desc")
    List<Booking> findAllByBookerIdAndStatus(Long userId, String status);

    @Query("select b from Booking b " +
            "left join Item i on (b.itemId = i.id) " +
            "where i.owner.id = (?1) order by b.start desc")
    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId);

    @Query("select b from Booking b " +
            "left join Item i on (b.itemId = i.id) " +
            "where i.owner.id = (?1) and b.end > (?2) order by b.start desc")
    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime dateTime);

    @Query("select b from Booking b " +
            "left join Item i on (b.itemId = i.id) " +
            "where i.owner.id = (?1) and (b.start > (?2) and b.end < (?2)) order by b.start asc")
    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartAsc(Long userId, LocalDateTime dateTime);

    @Query("select b from Booking b " +
            "left join Item i on (b.itemId = i.id) " +
            "where i.owner.id = (?1) and b.start > (?2) order by b.start desc ")
    List<Booking> findAllByItemOwnerIdAndStatusFuture(Long userId, LocalDateTime dateTime);

    @Query("select b from Booking b " +
            "left join Item i on (b.itemId = i.id) " +
            "where i.owner.id = (?1) and b.status like (?2) order by b.start desc")
    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, String status);
}

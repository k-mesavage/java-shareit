package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItemId(Long itemId);

    @Query("select b from Booking b " +
            "where b.booker.id = (?1) order by b.start desc")
    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = (?1) and b.end < (?2) order by b.start desc")
    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime dateTime, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId,
                                                                             LocalDateTime start,
                                                                             LocalDateTime end, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = (?1) and b.start > (?2) order by b.start desc")
    List<Booking> findAllByBookerIdAndStatusFuture(Long userId, LocalDateTime dateTime, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker.id = (?1) and b.status = (?2) order by b.start desc")
    List<Booking> findAllByBookerIdAndStatus(Long userId, String status, Pageable pageable);

    @Query("select b from Booking b " +
            "left join Item i on (b.item.id = i.id) " +
            "where i.owner.id = (?1) order by b.start desc")
    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId, Pageable pageable);

    @Query("select b from Booking b " +
            "left join Item i on (b.item.id = i.id) " +
            "where i.owner.id = (?1) and b.end < (?2) order by b.start desc")
    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime dateTime, Pageable pageable);

    @Query("select b from Booking b " +
            "left join Item i on (b.item.id = i.id) " +
            "where i.owner.id = (?1) and (b.start < (?2) and b.end > (?3)) order by b.start asc")
    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId,
                                                                                LocalDateTime start,
                                                                                LocalDateTime end, Pageable pageable);

    @Query("select b from Booking b " +
            "left join Item i on (b.item.id = i.id) " +
            "where i.owner.id = (?1) and b.start > (?2) order by b.start desc ")
    List<Booking> findAllByItemOwnerIdAndStatusFuture(Long userId, LocalDateTime dateTime, Pageable pageable);

    @Query("select b from Booking b " +
            "left join Item i on (b.item.id = i.id) " +
            "where i.owner.id = (?1) and b.status like (?2) order by b.start desc")
    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, String status, Pageable pageable);

    Booking findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(Long itemId,
                                                                     LocalDateTime dateTime,
                                                                     String status);

    Booking findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(Long itemId,
                                                                   LocalDateTime dateTime,
                                                                   String status);

    Booking findFirstByItemIdAndBookerIdAndStatusAndEndBefore(Long itemId,
                                                              Long userId,
                                                              String status,
                                                              LocalDateTime dateTime);
}

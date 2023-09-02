package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.params.BookingStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Builder
@Table(name = "bookings")
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "booker_id")
    private Long bookerId;

    @Column(name = "status")
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id)
                && Objects.equals(start, booking.start)
                && Objects.equals(end, booking.end)
                && Objects.equals(itemId, booking.itemId)
                && Objects.equals(bookerId, booking.bookerId)
                && Objects.equals(status, booking.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookerId);
    }
}

package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity(name = "bookings")
@Getter @Setter @ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    private LocalDate start;

    @Column(name = "end_date")
    private LocalDate end;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    private User booker;

    @Column(name = "status")
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) && Objects.equals(booker, booking.booker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, booker);
    }
}

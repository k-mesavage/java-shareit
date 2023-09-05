package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class ShortUserDto {

    @Id
    public Long id;

    public ShortUserDto(Long id) {
        this.id = id;
    }
}

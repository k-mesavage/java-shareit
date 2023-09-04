package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "items")
public class ShortItemDto {

    @Id
    public Long id;

    @Column(name = "name")
    public String name;

    public ShortItemDto() {
    }

    public ShortItemDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Long> {

    Item findByOwnerId(Long userId);

    List<Item> findAllByOwnerId(Long userId);

    List<Item> searchByNameLike(String name);
}

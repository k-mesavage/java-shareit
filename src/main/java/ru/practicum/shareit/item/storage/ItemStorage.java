package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemStorage extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long userId);

    @Query("select it from Item as it where " +
            "(lower(it.name) like lower(concat('%', :param, '%')) or " +
            "lower(it.description) like lower(concat('%', :param, '%')) ) ")
    List<Item> searchAvailableItems(@PathVariable("param") String param);
}

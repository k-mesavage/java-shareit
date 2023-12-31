package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestStorage extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequesterIdOrderByCreatedDesc(Long requesterId, Pageable pageable);

    @Query("select r from ItemRequest r")
    List<ItemRequest> getAll(Pageable pageable);
}
